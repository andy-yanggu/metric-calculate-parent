package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <V>
 */
@Data
@NoArgsConstructor
public class TimedKVMetricCube<V extends MergedUnit<V> & Value<?>, C extends TimedKVMetricCube<V, C>>
        implements MetricCube<TimeSeriesKVTable<V>, Long, V, C> {

    public static final String PREFIX = "MC.T.KV.C";

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

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

    @Override
    public String getPrefix() {
        return PREFIX;
    }

    /**
     * 当前指标唯一的key
     * 指标key + 指标维度
     *
     * @return
     */
    @Override
    public String getRealKey() {
        return getPrefix() + ":" + dimensionSet.realKey();
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
    public void put(Long key, V value) {
        table.putValue(key, null, value);
    }

    @Override
    public TimeSeriesKVTable<V> getTable() {
        return table;
    }

    @Override
    public Value query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return table.query(from, fromInclusive, to, toInclusive);
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

    @Override
    public long getReferenceTime() {
        return this.referenceTime;
    }

    @Override
    public void setReferenceTime(long referenceTime) {
        this.referenceTime = Math.max(this.referenceTime, referenceTime);
    }

}
