package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

import static com.yanggu.metric_calculate.core.enums.TimeUnit.MILLS;

/**
 * 时间序列存储
 * <p>key是聚合后的时间戳, value是MergedUnit</p>
 *
 * @param <V>
 */
@Data
public class TimeSeriesKVTable<V extends MergedUnit<V> & Value<?>> extends TreeMap<Long, V>
        implements Table<Long, V, Long, V, TimeSeriesKVTable<V>>, Serializable {

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    @Override
    public void putValue(Long rowKey, Long column, V value) {
        rowKey = getActual(rowKey);
        compute(rowKey, (k, v) -> v == null ? value : v.merge(value));
    }

    @Override
    public TimeSeriesKVTable<V> merge(TimeSeriesKVTable<V> that) {
        for (Map.Entry<Long, V> timeRow : that.entrySet()) {
            V thisRow = get(timeRow.getKey());
            if (thisRow == null) {
                put(timeRow.getKey(), timeRow.getValue());
            } else if (thisRow.getClass().equals(timeRow.getValue().getClass())) {
                thisRow.merge(timeRow.getValue());
            }
        }
        return this;
    }

    @Override
    public V query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(from, fromInclusive, to, toInclusive);

        if (CollUtil.isEmpty(subMap)) {
            return null;
        }

        return subMap.values().stream()
                .reduce(MergedUnit::merge)
                .orElseThrow(() -> new RuntimeException("merge失败"));
    }

    public int eliminateExpiredData(long minTimestamp) {
        int dataCount = 0;
        Iterator<Map.Entry<Long, V>> iterator = this.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getKey() < minTimestamp) {
                iterator.remove();
                dataCount++;
            }
        }
        return dataCount;
    }

    private Long getActual(Long timestamp) {
        //如果不是毫秒值, 格式化到时间单位
        if (!timeBaselineDimension.getUnit().equals(MILLS)) {
            timestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        }
        return timestamp;
    }

    @Override
    public TimeSeriesKVTable<V> cloneEmpty() {
        TimeSeriesKVTable<V> result = new TimeSeriesKVTable<>();
        result.setTimeBaselineDimension(this.timeBaselineDimension);
        return result;
    }

    @Override
    public TimeSeriesKVTable<V> fastClone() {
        TimeSeriesKVTable<V> result = cloneEmpty();
        forEach((k, v) -> result.put(k, v.fastClone()));
        return result;
    }

    public TimeSeriesKVTable<V> subTable(Long start, boolean fromInclusive, Long end, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(start, fromInclusive, end, toInclusive);
        TimeSeriesKVTable<V> cloneEmpty = cloneEmpty();
        subMap.forEach((k, v) -> cloneEmpty.put(k, v.fastClone()));
        return cloneEmpty;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (timeBaselineDimension != null ? timeBaselineDimension.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TimeSeriesKVTable<?> that = (TimeSeriesKVTable<?>) o;
        return Objects.equals(timeBaselineDimension, that.timeBaselineDimension);
    }

}
