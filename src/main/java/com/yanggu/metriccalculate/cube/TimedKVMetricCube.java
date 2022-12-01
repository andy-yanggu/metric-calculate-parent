package com.yanggu.metriccalculate.cube;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Tuple;
import com.yanggu.client.magiccube.enums.TimeUnit;
import com.yanggu.metriccalculate.fieldprocess.DimensionSet;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.Value;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @param <V>
 */
@Data
@NoArgsConstructor
public class TimedKVMetricCube<V extends MergedUnit<V> & Value<?>, C extends TimedKVMetricCube<V, C>> implements MetricCube<TimeSeriesKVTable<V>, Long, V, C, TimeUnit> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 当前数据聚合时间戳
     */
    private long referenceTime;

    /**
     * 指标维度
     */
    private DimensionSet dimensionSet;

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 时间序列存储
     */
    private TimeSeriesKVTable<V> table;

    /**
     * 当前指标唯一的key
     * 指标名称 + 指标维度
     *
     * @return
     */
    @Override
    public String getRealKey() {
        return name + ":" + dimensionSet.getDimensionMap().values().stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String key() {
        return dimensionSet.getDimensionMap().values().stream().map(String::valueOf).collect(Collectors.joining(","));
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
    public DimensionSet dimensions() {
        return dimensionSet;
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
    public Value<V> query() {
        return table.query(referenceTime - this.timeBaselineDimension.realLength(), false, referenceTime, true);
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
        this.referenceTime = that.getReferenceTime();
        return (C) this;
    }

    /**
     * 获取时间窗口
     * WindowStart和WindowEnd, 包含Start, 不包含End, 左闭右开
     *
     * @return
     */
    public Tuple getTimeWindow() {
        long windowEnd = DateUtil.ceiling(new Date(referenceTime), timeBaselineDimension.getUnit().getDateField()).getTime() + 1;
        long windowStart = windowEnd - timeBaselineDimension.realLength();
        return new Tuple(windowStart, windowEnd);
    }

}
