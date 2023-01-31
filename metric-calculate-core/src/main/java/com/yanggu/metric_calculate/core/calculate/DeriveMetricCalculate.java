package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.cube.*;
import com.yanggu.metric_calculate.core.fieldprocess.*;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.RoundAccuracy;
import com.yanggu.metric_calculate.core.pojo.Store;
import com.yanggu.metric_calculate.core.table.CountWindowTable;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
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
public class DeriveMetricCalculate<M extends MergedUnit<M> & Value<?>>
        implements Calculate<JSONObject, MetricCube<Table, Long, M, ?>> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterProcessor filterProcessor;

    /**
     * 聚合字段处理器, 生成MergeUnit
     */
    private BaseAggregateFieldProcessor<M> aggregateFieldProcessor;

    /**
     * 对于滑动计数窗口函数, 需要进行二次聚合计算
     */
    private BaseAggregateFieldProcessor<?> subAggregateFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 时间聚合粒度。包含时间单位和时间长度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 维度字段处理器, 从明细数据中提取出维度数据
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 存储宽表, 用于指标存储相关信息
     */
    private Store store;

    /**
     * 派生指标中间结算结果类
     */
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @SneakyThrows
    @Override
    public MetricCube<Table, Long, M, ?> exec(JSONObject input) {
        //执行前置过滤条件
        Boolean filter = filterProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return null;
        }

        //执行聚合字段处理器, 生成MergeUnit
        M process = (M) aggregateFieldProcessor.process(input);
        if (process == null) {
            if (log.isDebugEnabled()) {
                log.debug("Get unit from input, but get null, input = {}", JSONUtil.toJsonStr(input));
            }
            return null;
        }

        //数据的时间戳
        Long timestamp = timeFieldProcessor.process(input);

        //维度数据
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        MergeType annotation = aggregateFieldProcessor.getMergeUnitClazz().getAnnotation(MergeType.class);

        boolean countWindow = annotation.countWindow();
        MetricCube<Table, Long, M, ?> metricCube;
        //如果是滑动计数窗口需要使用CountWindowMetricCube
        if (countWindow) {
            metricCube = new CountWindowMetricCube<>();
            CountWindowTable<M> table = new CountWindowTable<>();
            metricCube.setTable(table);
        } else {
            TimeSeriesKVTable<M> table = new TimeSeriesKVTable<>();
            table.setTimeBaselineDimension(timeBaselineDimension);
            metricCube = new TimedKVMetricCube();
            metricCube.setTable(table);
        }
        metricCube.setName(name);
        metricCube.setKey(key);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.setDimensionSet(dimensionSet);
        //设置数据当前聚合时间
        metricCube.setReferenceTime(timeBaselineDimension.getCurrentAggregateTimestamp(timestamp));
        metricCube.put(timestamp, process);
        return metricCube;
    }

    @SneakyThrows
    public List<DeriveMetricCalculateResult> query(MetricCube newMetricCube) {
        MetricCube metricCube = deriveMetricMiddleStore.get(newMetricCube);
        if (metricCube == null) {
            metricCube = newMetricCube;
        } else {
            metricCube.merge(newMetricCube);
        }
        //删除过期数据
        metricCube.eliminateExpiredData();
        //更新中间状态数据
        deriveMetricMiddleStore.put(metricCube);

        //获取统计的时间窗口
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindow(metricCube.getReferenceTime());
        if (CollUtil.isEmpty(timeWindowList)) {
            return Collections.emptyList();
        }

        List<DeriveMetricCalculateResult> list = new ArrayList<>();
        for (TimeWindow timeWindow : timeWindowList) {

            DeriveMetricCalculateResult result = new DeriveMetricCalculateResult();
            list.add(result);
            //指标名称
            result.setName(metricCube.getName());

            //指标key
            result.setKey(metricCube.getKey());

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

            //如果是滑动计数窗口需要进行二次聚合处理
            MergeType annotation = aggregateFieldProcessor.getMergeUnitClazz().getAnnotation(MergeType.class);
            if (annotation.countWindow() && value instanceof List) {
                List<JSONObject> tempValueList = (List<JSONObject>) value;
                MergedUnit mergedUnit = tempValueList.stream()
                        .map(tempValue -> {
                            try {
                                return (MergedUnit) subAggregateFieldProcessor.process(tempValue);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .reduce((a, b) -> a.merge(b))
                        .orElseThrow(() -> new RuntimeException("mergeUnit的merge方法执行失败"));
                value = ValueMapper.value(((Value<?>) mergedUnit));
            }

            //处理精度
            value = RoundAccuracyUtil.handlerRoundAccuracy(value, roundAccuracy);
            result.setResult(value);

            if (log.isDebugEnabled()) {
                String collect = metricCube.getDimensionSet().getDimensionMap().entrySet().stream()
                        .map(tempEntry -> tempEntry.getKey() + ":" + tempEntry.getValue())
                        .collect(Collectors.joining(","));
                log.debug("指标名称: " + result.getName() +
                        ", 指标key: " + result.getKey() +
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
