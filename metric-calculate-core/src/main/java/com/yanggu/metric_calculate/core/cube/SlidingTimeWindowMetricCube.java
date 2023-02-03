package com.yanggu.metric_calculate.core.cube;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.table.SlidingTimeWindowTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.List;

@Data
public class SlidingTimeWindowMetricCube<V extends MergedUnit<V> & Value<?>>
        implements MetricCube<SlidingTimeWindowTable<V>, Long, V, SlidingTimeWindowMetricCube<V>> {

    public static final String PREFIX = "MC.S.TW.C";

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
    private SlidingTimeWindowTable<V> table;

    @Override
    public SlidingTimeWindowMetricCube<V> init() {
        return null;
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
    public Value query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return table.query(from, fromInclusive, to, toInclusive);
    }

    @Override
    public SlidingTimeWindowMetricCube<V> merge(SlidingTimeWindowMetricCube<V> that) {
        table.merge(that.getTable());
        return this;
    }

    @Override
    public SlidingTimeWindowTable<V> getTable() {
        return table;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public SlidingTimeWindowMetricCube<V> cloneEmpty() {
        return null;
    }

    @Override
    public int eliminateExpiredData() {
        return 0;
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
        return null;
    }

}
