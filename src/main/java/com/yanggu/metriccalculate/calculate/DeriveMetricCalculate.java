package com.yanggu.metriccalculate.calculate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.RoundAccuracy;
import com.yanggu.client.magiccube.pojo.Store;
import com.yanggu.metriccalculate.cube.DeriveMetricMiddleStore;
import com.yanggu.metriccalculate.cube.MetricCube;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.fieldprocess.*;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.util.RoundAccuracyUtil;
import com.yanggu.metriccalculate.value.Value;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 派生指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class DeriveMetricCalculate<M extends MergedUnit<M> & Value<?>> implements Calculate<JSONObject, TimedKVMetricCube<M, ? extends TimedKVMetricCube>> {

    /**
     * 指标名称
     */
    private String name;

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
        if (Boolean.FALSE.equals(filter) && log.isDebugEnabled()) {
            log.debug("Input discard, input = {}", JSONUtil.toJsonStr(input));
            return null;
        }

        //执行聚合字段处理器, 生成MergeUnit
        M process = aggregateFieldProcessor.process(input);
        if (process == null && log.isDebugEnabled()) {
            log.debug("Get unit from input, but get null, input = {}", JSONUtil.toJsonStr(input));
            return null;
        }

        //数据的时间戳
        Long timestamp = timeFieldProcessor.process(input);

        //维度数据
        DimensionSet dimensionSet = dimensionSetProcessor.process(input);

        //生成cube, 并且放入缓存中, 如果缓存中存在之前的数据, 则进行合并
        //本地缓存的目的是为了进行批处理计算, 进行本地聚合操作
        return cache.compute(dimensionSet, (k, v) -> {
            if (v == null) {
                TimeSeriesKVTable<M> table = new TimeSeriesKVTable<>();
                table.setTimeBaselineDimension(timeBaselineDimension);
                v = new TimedKVMetricCube<>();
                v.setName(name);
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

    public DeriveMetricCalculateResult query(TimedKVMetricCube newMetricCube) {
        TimedKVMetricCube metricCube = (TimedKVMetricCube) deriveMetricMiddleStore.get(newMetricCube.getRealKey());
        if (metricCube == null) {
            metricCube = newMetricCube;
        } else {
            metricCube.merge(newMetricCube);
        }
        Object value = metricCube.query().value();
        //处理精度
        value = RoundAccuracyUtil.handlerRoundAccuracy(value, roundAccuracy);

        Tuple timeWindow = metricCube.getTimeWindow();
        Object windowStart = timeWindow.get(0);
        Object windowEnd = timeWindow.get(1);

        DeriveMetricCalculateResult deriveMetricCalculateResult = new DeriveMetricCalculateResult();
        //指标名称
        deriveMetricCalculateResult.setName(metricCube.getName());
        //指标维度
        deriveMetricCalculateResult.setDimensionMap(((LinkedHashMap) metricCube.getDimensionSet().getDimensionMap()));
        //窗口开始时间
        deriveMetricCalculateResult.setStartTime(DateUtil.formatDateTime(new Date(Long.parseLong(windowStart.toString()))));
        //窗口结束时间
        deriveMetricCalculateResult.setEndTime(DateUtil.formatDateTime(new Date(Long.parseLong(windowEnd.toString()))));
        //聚合值
        deriveMetricCalculateResult.setResult(value);

        if (log.isDebugEnabled()) {
            String collect = metricCube.getDimensionSet().getDimensionMap().entrySet().stream()
                    .map(tempEntry -> tempEntry.getKey() + ":" + tempEntry.getValue())
                    .collect(Collectors.joining(","));
            log.debug("指标名称: " + metricCube.getName() +
                    ", 指标维度: " + collect +
                    ", 窗口开始时间: " + DateUtil.formatDateTime(new Date(Long.parseLong(windowStart.toString()))) +
                    ", 窗口结束时间: " + DateUtil.formatDateTime(new Date(Long.parseLong(windowEnd.toString()))) +
                    ", 聚合值: " + value);
        }
        return deriveMetricCalculateResult;
    }

}
