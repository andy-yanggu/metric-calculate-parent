package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.pojo.RoundAccuracy;
import com.yanggu.metric_calculate.core.pojo.Store;
import com.yanggu.metric_calculate.core.store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.cube.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.fieldprocess.*;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.RoundAccuracyUtil;
import com.yanggu.metric_calculate.core.value.Value;
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
        implements Calculate<JSONObject, TimedKVMetricCube<M, ? extends TimedKVMetricCube>> {

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
    private AggregateFieldProcessor<M> aggregateFieldProcessor;

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
     * 本地缓存, 用于批计算处理
     */
    private Map<DimensionSet, TimedKVMetricCube<M, ? extends TimedKVMetricCube<M, ?>>> cache;

    /**
     * 派生指标中间结算结果类
     */
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void init(TaskContext taskContext) throws RuntimeException {
        cache = taskContext.cache();
    }

    @SneakyThrows
    @Override
    public TimedKVMetricCube<M, ? extends TimedKVMetricCube> exec(JSONObject input) {
        //执行前置过滤条件
        Boolean filter = filterProcessor.process(input);
        if (Boolean.FALSE.equals(filter)) {
            return null;
        }

        //执行聚合字段处理器, 生成MergeUnit
        M process = aggregateFieldProcessor.process(input);
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

        //生成cube, 并且放入缓存中, 如果缓存中存在之前的数据, 则进行合并
        //本地缓存的目的是为了加快处理速度, 进行本地聚合操作
        //可以对exec进行批量for循环调用, 进行本地批处理
        return cache.compute(dimensionSet, (k, v) -> {
            if (v == null) {
                TimeSeriesKVTable<M> table = new TimeSeriesKVTable<>();
                table.setTimeBaselineDimension(timeBaselineDimension);
                v = new TimedKVMetricCube<>();
                v.setName(name);
                v.setKey(key);
                v.setTimeBaselineDimension(timeBaselineDimension);
                v.setDimensionSet(dimensionSet);
                v.setTable(table);
            }
            //设置数据当前聚合时间
            v.setReferenceTime(timeBaselineDimension.getCurrentAggregateTimestamp(timestamp));
            v.put(timestamp, process);
            return v;
        });
    }

    public List<DeriveMetricCalculateResult> query(TimedKVMetricCube newMetricCube) {
        String realKey = newMetricCube.getRealKey();
        TimedKVMetricCube metricCube = (TimedKVMetricCube) deriveMetricMiddleStore.get(realKey);
        if (metricCube == null) {
            metricCube = newMetricCube;
        } else {
            metricCube.merge(newMetricCube);
        }
        //删除过期数据
        metricCube.eliminateExpiredData();
        //更新中间状态数据
        deriveMetricMiddleStore.put(realKey, metricCube);

        //获取统计的时间窗口
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindow(metricCube.getReferenceTime());
        Long startWindow = CollUtil.getFirst(timeWindowList).getStart();
        Long endWindow = CollUtil.getLast(timeWindowList).getEnd();
        TimeSeriesKVTable<?> timeSeriesKVTable = metricCube.getTable().subTable(startWindow, true, endWindow, false);

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
            long windowStart = timeWindow.getStart();
            result.setStartTime(DateUtil.formatDateTime(new Date(windowStart)));

            //窗口结束时间
            long windowEnd = timeWindow.getEnd();
            result.setEndTime(DateUtil.formatDateTime(new Date(windowEnd)));

            //聚合值
            Object value = timeSeriesKVTable.query(windowStart, windowEnd).value();

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
