package com.yanggu.metriccalculate.cube;

import com.yanggu.client.magiccube.enums.TimeUnit;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.Value;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @param <V>
 */
@Data
public class TimedKVMetricCube<V extends MergedUnit<V> & Value<V>, C extends TimedKVMetricCube<V, C>>
        implements MetricCube<TimeSeriesKVTable<V>, Long, V, C, TimeUnit> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标维度
     */
    private Map<String, Object> dimensionMap;

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    private TimeSeriesKVTable<V> table;

    /**
     * 当前指标唯一的key
     * 指标名称 + 指标维度
     *
     * @return
     */
    @Override
    public String getRealKey() {
        return name + ":" + dimensionMap.values().stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String key() {
        return dimensionMap.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public boolean isEmpty() {
        return table.isEmpty();
    }

    @Override
    public C cloneEmpty() {
        return null;
    }

    @Override
    public Cube init() {
        return null;
    }

    @Override
    public Map<String, Object> dimensions() {
        return dimensionMap;
    }

    @Override
    public TimeBaselineDimension baselineDimension() {
        return timeBaselineDimension;
    }

    @Override
    public V put(Long key, V value) {
        return table.putValue(key, value);
    }

    @Override
    public TimeSeriesKVTable<V> table() {
        return table;
    }

    @Override
    public TimeSeriesKVTable<V> table(String key) {
        return null;
    }

    @Override
    public MetricCube table(String key, TimeSeriesKVTable<V> table) {
        return null;
    }

    @Override
    public Value<V> query() {
        return null;
    }

    @Override
    public Value<V> query(long end) {
        return null;
    }

    @Override
    public Value<V> query(long start, long end) {
        return null;
    }

    @Override
    public void expire(long expire) {

    }

    @Override
    public long expire() {
        return 0;
    }

    @Override
    public int eliminateExpiredData() {
        return 0;
    }

    @Override
    public C fastClone() {
        return null;
    }

    @Override
    public C merge(C that) {
        if (that == null) {
            return (C) this;
        }
        table.merge(that.getTable());
        return (C) this;
    }

    @Override
    public long referenceTime() {
        return 0;
    }

    @Override
    public void referenceTime(long referenceTime) {

    }
}
