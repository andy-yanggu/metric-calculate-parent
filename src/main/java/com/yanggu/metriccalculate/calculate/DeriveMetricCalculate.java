package com.yanggu.metriccalculate.calculate;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.RoundAccuracy;
import com.yanggu.client.magiccube.pojo.Store;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.fieldprocess.*;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.Value;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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
     * 聚合字段处理器
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
     * 维度字段处理器
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 存储宽表
     */
    private Store store;

    /**
     * 本地缓存, 用于批计算处理
     */
    private Map<DimensionSet, TimedKVMetricCube<M, ? extends TimedKVMetricCube<M, ?>>> cache;

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
                TimeSeriesKVTable<M> timeSeriesKVTable = new TimeSeriesKVTable<>();
                timeSeriesKVTable.setTimeBaselineDimension(timeBaselineDimension);
                v = new TimedKVMetricCube<>();
                v.setName(name);
                v.setReferenceTime(timeBaselineDimension.getCurrentAggregateTimestamp(timestamp));
                v.setTimeBaselineDimension(timeBaselineDimension);
                v.setDimensionSet(dimensionSet);
                v.setTable(timeSeriesKVTable);
            }
            v.put(timestamp, process);
            return v;
        });
    }

}
