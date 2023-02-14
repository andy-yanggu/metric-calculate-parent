package com.yanggu.metric_calculate.core.cube;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.pojo.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.SlidingTimeWindowTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.List;

@Data
public class SlidingTimeWindowMetricCube<V extends MergedUnit<V> & Value<?>>
        implements MetricCube<SlidingTimeWindowTable<V>, Long, V, SlidingTimeWindowMetricCube<V>> {

    private static final String PREFIX = "MC.S.TW.C";

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

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
    private SlidingTimeWindowTable<V> table;

    @Override
    public SlidingTimeWindowMetricCube<V> init() {
        table = new SlidingTimeWindowTable<>();
        return this;
    }

    @Override
    public void put(Long key, V value) {
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(key);
        if (CollUtil.isEmpty(timeWindow)) {
            return;
        }
        timeWindow.forEach(tempTimeWindow -> {
            long windowStart = tempTimeWindow.getWindowStart();
            long windowEnd = tempTimeWindow.getWindowEnd();
            table.putValue(windowStart, windowEnd, value.fastClone());
        });
    }

    @Override
    public SlidingTimeWindowMetricCube<V> merge(SlidingTimeWindowMetricCube<V> that) {
        table.merge(that.getTable());
        return this;
    }

    @Override
    public Value<?> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return table.query(from, fromInclusive, to, toInclusive);
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
    public SlidingTimeWindowMetricCube<V> cloneEmpty() {
        SlidingTimeWindowMetricCube<V> metricCube = new SlidingTimeWindowMetricCube<>();
        metricCube.setKey(key);
        metricCube.setName(name);
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setTimeBaselineDimension(timeBaselineDimension);
        metricCube.init();
        return metricCube;
    }

    @Override
    public int eliminateExpiredData() {
        long minTimestamp = getReferenceTime() - 2 * timeBaselineDimension.realLength();
        return table.eliminateExpiredData(minTimestamp);
    }

    @Override
    public long getReferenceTime() {
        return this.referenceTime;
    }

    @Override
    public void setReferenceTime(long referenceTime) {
        this.referenceTime = Math.max(this.referenceTime, referenceTime);
    }

    @Override
    public SlidingTimeWindowMetricCube<V> fastClone() {
        SlidingTimeWindowMetricCube<V> metricCube = cloneEmpty();
        metricCube.setReferenceTime(referenceTime);
        metricCube.setTable(table.fastClone());
        return metricCube;
    }

}
