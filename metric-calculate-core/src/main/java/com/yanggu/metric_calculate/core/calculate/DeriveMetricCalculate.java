package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.cube.MetricCubeFactory;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.RoundAccuracy;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core.pojo.store.StoreInfo;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.RoundAccuracyUtil;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 派生指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class DeriveMetricCalculate<T, M extends MergedUnit<M> & Value<?>>
        implements Calculate<T, MetricCube<Table, Long, M, ?>> {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterFieldProcessor<T> filterFieldProcessor;

    /**
     * 聚合字段处理器, 生成MergeUnit
     */
    private AggregateFieldProcessor<T, M> aggregateFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor<T> timeFieldProcessor;

    /**
     * 时间聚合粒度。包含时间单位和时间长度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 维度字段处理器, 从明细数据中提取出维度数据
     */
    private DimensionSetProcessor<T> dimensionSetProcessor;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

    /**
     * 派生指标中间结算结果存储
     */
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    /**
     * 用于生成cube的工厂类
     */
    private MetricCubeFactory<M> metricCubeFactory;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 存储宽表, 用于指标存储相关信息
     */
    private StoreInfo storeInfo;

    @SneakyThrows
    @Override
    public MetricCube<Table, Long, M, ?> exec(T input) {
        //执行前置过滤条件
        Boolean filter = filterFieldProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return null;
        }

        //执行聚合字段处理器, 生成MergeUnit
        M process = aggregateFieldProcessor.process(input);
        if (process == null) {
            return null;
        }

        //数据的时间戳
        Long timestamp = timeFieldProcessor.process(input);

        //维度数据
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //当前数据的聚合时间戳
        Long referenceTime = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);

        //生成cube
        MetricCube<Table, Long, M, ?> metricCube = metricCubeFactory.createMetricCube(dimensionSet, referenceTime);

        //放入生成的MergeUnit
        metricCube.put(timestamp, process);
        return metricCube;
    }

    /**
     * 无状态查询
     *
     * @param newMetricCube
     * @return
     */
    public MetricCube noState(MetricCube newMetricCube) {
        //调用中计算结果存储的查询方法
        MetricCube metricCube = deriveMetricMiddleStore.get(newMetricCube);
        //如果无状态且不包含当前笔, 直接返回历史数据
        if (Boolean.FALSE.equals(includeCurrent)) {
            return metricCube;
        }
        //包含当前笔, 需要进行merge
        if (metricCube == null) {
            metricCube = newMetricCube;
        } else {
            metricCube.merge(newMetricCube);
        }
        return metricCube;
    }

    /**
     * 更新状态
     *
     * @param newMetricCube
     * @return
     */
    public MetricCube updateState(MetricCube newMetricCube) {
        //调用中计算结果存储的查询方法
        MetricCube metricCube = deriveMetricMiddleStore.get(newMetricCube);
        if (metricCube == null) {
            metricCube = newMetricCube;
        } else {
            metricCube.merge(newMetricCube);
            //删除过期数据
            metricCube.eliminateExpiredData();
        }
        //更新中间状态数据
        deriveMetricMiddleStore.update(metricCube);
        return metricCube;
    }

    /**
     * 查询操作, 查询出指标数据
     *
     * @param metricCube
     * @return
     */
    public List<DeriveMetricCalculateResult> query(MetricCube metricCube) {
        if (metricCube == null) {
            return Collections.emptyList();
        }

        //获取统计的时间窗口
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindow(metricCube.getReferenceTime());
        if (CollUtil.isEmpty(timeWindowList)) {
            return Collections.emptyList();
        }

        List<DeriveMetricCalculateResult> list = new ArrayList<>();
        for (TimeWindow timeWindow : timeWindowList) {

            DeriveMetricCalculateResult result = new DeriveMetricCalculateResult();
            //指标key
            result.setKey(metricCube.getKey());

            //指标名称
            result.setName(metricCube.getName());

            //指标维度
            result.setDimensionMap(((LinkedHashMap) metricCube.getDimensionSet().getDimensionMap()));

            //窗口开始时间
            long windowStart = timeWindow.getWindowStart();
            result.setStartTime(DateUtil.formatDateTime(new Date(windowStart)));

            //窗口结束时间
            long windowEnd = timeWindow.getWindowEnd();
            result.setEndTime(DateUtil.formatDateTime(new Date(windowEnd)));

            //聚合值
            Value<?> query = metricCube.query(windowStart, true, windowEnd, false);
            Object value = ValueMapper.value(query);

            if (value == null) {
                continue;
            }

            //进行回调, 对于滑动计数窗口和CEP需要额外的处理
            value = aggregateFieldProcessor.callBack(value);
            if (value == null) {
                continue;
            }

            //处理精度
            value = RoundAccuracyUtil.handlerRoundAccuracy(value, roundAccuracy);
            result.setResult(value);

            list.add(result);
            if (log.isDebugEnabled()) {
                //拼接维度数据
                String collect = metricCube.getDimensionSet().getDimensionMap().entrySet().stream()
                        .map(tempEntry -> tempEntry.getKey() + ":" + tempEntry.getValue())
                        .collect(Collectors.joining(","));
                log.debug("指标key: " + result.getKey() +
                        ", 指标名称: " + result.getName() +
                        ", 指标维度: " + collect +
                        ", 窗口开始时间: " + DateUtil.formatDateTime(new Date(windowStart)) +
                        ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(windowEnd)) +
                        ", 聚合方式: " + aggregateFieldProcessor.getAggregateType() +
                        ", 聚合值: " + value);
            }
        }
        return list;
    }

}
