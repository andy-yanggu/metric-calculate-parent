package com.yanggu.metriccalculate.cube;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.unit.collection.ListUnit;
import com.yanggu.metriccalculate.value.NoneValue;
import com.yanggu.metriccalculate.value.TimeReferable;
import com.yanggu.metriccalculate.value.Value;
import lombok.Data;

import java.util.*;

/**
 * 时间序列存储
 * key是聚合后的时间戳, value是MergedUnit
 *
 * @param <V>
 */
@Data
public class TimeSeriesKVTable<V extends MergedUnit<V> & Value<?>> extends TreeMap<Long, V> implements KVTable<Long, V, TimeSeriesKVTable<V>>, SortedTable<Long, V, Long, V, TimeSeriesKVTable<V>>,
        TimeReferable {

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
            if (thisRow instanceof ListUnit) {
                Iterator<V> mergeableIterator = ((ListUnit) thisRow).iterator();
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
    public long count() {
        return size();
    }

    @Override
    public void truncate() {
        clear();
    }

    @Override
    public Table<Long, V, Long, V, TimeSeriesKVTable<V>> cloneEmpty() {
        return null;
    }

    @Override
    public TimeSeriesKVTable<V> fastClone() {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Value query(Long from, Long to) {
        return null;
    }


    protected Value query(long from, boolean fromInclusive,
                          long to, boolean toInclusive) {
        NavigableMap<Long, V> subMap = subMap(from, fromInclusive, to, toInclusive);

        Collection<V> values = subMap.values();

        if (CollUtil.isEmpty(values)) {
            return NoneValue.INSTANCE;
        }

        return values.stream().reduce(MergedUnit::merge).get();
    }

    @Override
    public V firstRow() {
        return null;
    }

    @Override
    public V lastRow() {
        return null;
    }

    @Override
    public Value first() {
        return null;
    }

    @Override
    public Value last() {
        return null;
    }

    @Override
    public TimeSeriesKVTable<V> subTable(Long from, Long to) {
        return null;
    }

    @Override
    public TimeSeriesKVTable<V> subTable(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return null;
    }

    @Override
    public long referenceTime() {
        return 0;
    }

    @Override
    public void referenceTime(long referenceTime) {

    }
}
