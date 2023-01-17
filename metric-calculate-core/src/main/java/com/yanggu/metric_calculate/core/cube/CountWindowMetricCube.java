package com.yanggu.metric_calculate.core.cube;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.List;

@Data
public class CountWindowMetricCube<V extends MergedUnit<V> & Value<?>, C extends CountWindowMetricCube<V, C>>
        implements MetricCube<CountWindowTable<V>, Long, V, C> {

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
    private Table table;

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
    public String name() {
        return name;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public void put(Long key, V value) {
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(key);
        if (CollUtil.isEmpty(timeWindow)) {
            return;
        }
        timeWindow.forEach(tempTimeWindow -> table.putValue(tempTimeWindow.getStart(), tempTimeWindow.getEnd(), value));
    }

    @Override
    public Value query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return table.query(from, fromInclusive, to, toInclusive);
    }

    @Override
    public C merge(C that) {
        table.merge(that.getTable());
        return (C) this;
    }

    @Override
    public CountWindowTable<V> table() {
        return (CountWindowTable<V>) table;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
    public int eliminateExpiredData() {
        return 0;
    }

    @Override
    public long getReferenceTime() {
        return 0;
    }

    @Override
    public void setReferenceTime(long referenceTime) {

    }

    @Override
    public C fastClone() {
        return null;
    }

}
