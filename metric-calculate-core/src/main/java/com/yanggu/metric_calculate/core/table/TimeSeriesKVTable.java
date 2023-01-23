package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.cube.TimeReferable;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.*;

/**
 * 时间序列存储
 * key是聚合后的时间戳, value是MergedUnit
 *
 * @param <V>
 */
@Data
public class TimeSeriesKVTable<V extends MergedUnit<V> & Value<?>> extends TreeMap<Long, V>
        implements KVTable<Long, V, TimeSeriesKVTable<V>>, TimeReferable {

    /**
     * 时间聚合粒度
     */
    private TimeBaselineDimension timeBaselineDimension;

    @Override
    public V putValue(Long key, V value) {
        key = timeBaselineDimension.getCurrentAggregateTimestamp(key);
        return compute(key, (k, v) -> v == null ? value : v.merge(value));
    }

    @Override
    public V getValue(Long key) {
        return get(timeBaselineDimension.getCurrentAggregateTimestamp(key));
    }

    @Override
    public boolean existValue(Long key) {
        return containsKey(timeBaselineDimension.getCurrentAggregateTimestamp(key));
    }

    @Override
    public V removeValue(Long key) {
        return remove(timeBaselineDimension.getCurrentAggregateTimestamp(key));
    }

    @Override
    public TimeSeriesKVTable<V> merge(TimeSeriesKVTable<V> that) {
        for (Map.Entry<Long, V> timeRow : that.entrySet()) {
            MergedUnit thisRow = get(timeRow.getKey());
            if (thisRow == null) {
                put(timeRow.getKey(), timeRow.getValue());
            } else if (thisRow.getClass().equals(timeRow.getValue().getClass())) {
                thisRow.merge(timeRow.getValue());
            }
            if (thisRow instanceof ListObjectUnit) {
                Iterator<V> mergeableIterator = ((ListObjectUnit) thisRow).iterator();
                V row = mergeableIterator.next();
                while (mergeableIterator.hasNext()) {
                    row.merge(mergeableIterator.next());
                }
                put(timeRow.getKey(), row);
            }
        }
        return this;
    }

    @Override
    public TimeSeriesKVTable<V> cloneEmpty() {
        TimeSeriesKVTable<V> result = new TimeSeriesKVTable();
        result.timeBaselineDimension = timeBaselineDimension;
        return result;
    }

    @Override
    public TimeSeriesKVTable<V> fastClone() {
        return null;
    }

    @Override
    public Value<?> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(from, fromInclusive, to, toInclusive);

        Collection<V> values = subMap.values();

        if (CollUtil.isEmpty(values)) {
            return NoneValue.INSTANCE;
        }

        return values.stream().reduce(MergedUnit::merge).get();
    }

    @Override
    public long getReferenceTime() {
        return lastKey();
    }

    @Override
    public void setReferenceTime(long referenceTime) {

    }

}
