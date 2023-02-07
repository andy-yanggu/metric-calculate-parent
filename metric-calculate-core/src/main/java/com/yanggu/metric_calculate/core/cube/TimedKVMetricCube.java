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
public class TimedKVMetricCube<V extends MergedUnit<V> & Value<?>>
        implements MetricCube<TimeSeriesKVTable<V>, Long, V, TimedKVMetricCube<V>> {

    private static final String PREFIX = "MC.T.KV.C";

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标维度
     */
    private DimensionSet dimensionSet;

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 当前数据聚合时间戳
     */
    private long referenceTime;

    /**
     * 时间序列存储
     */
    private TimeSeriesKVTable<V> table;

    @Override
    public TimedKVMetricCube<V> init() {
        TimeSeriesKVTable<V> timeSeriesKVTable = new TimeSeriesKVTable<>();
        timeSeriesKVTable.setTimeBaselineDimension(timeBaselineDimension);
        table = timeSeriesKVTable;
        return this;
    }

    @Override
    public void put(Long key, V value) {
        table.putValue(key, value);
    }

    @Override
    public TimedKVMetricCube<V> merge(TimedKVMetricCube<V> that) {
        if (that == null) {
            return this;
        }
        table.merge(that.getTable());
        this.referenceTime = that.getReferenceTime();
        return this;
    }

    @Override
    public Value query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return table.query(from, fromInclusive, to, toInclusive);
    }

    @Override
    public int eliminateExpiredData() {
        long minTimestamp = getReferenceTime() - 2 * timeBaselineDimension.realLength();
        return table.eliminateExpiredData(minTimestamp);
    }

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
    public TimedKVMetricCube<V> cloneEmpty() {
        TimedKVMetricCube<V> metricCube = new TimedKVMetricCube<>();
        metricCube.setKey(key);
        metricCube.setName(name);
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.init();
        return metricCube;
    }

    @Override
    public TimeSeriesKVTable<V> getTable() {
        return table;
    }

    @Override
    public TimedKVMetricCube<V> fastClone() {
        TimedKVMetricCube<V> metricCube = cloneEmpty();
        metricCube.setReferenceTime(referenceTime);
        metricCube.setTable(table.fastClone());
        return metricCube;
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
