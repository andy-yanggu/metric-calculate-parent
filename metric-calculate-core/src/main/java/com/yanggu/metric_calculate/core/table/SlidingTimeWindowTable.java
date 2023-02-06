package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.lang.Tuple;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SlidingTimeWindowTable<V extends MergedUnit<V> & Value<?>>
        implements Table<Long, V, Long, V, SlidingTimeWindowTable<V>>, Serializable {

    private final Map<Tuple, V> twoKeyTable = new HashMap<>();

    @Override
    public V putValue(Long rowKey, Long column, V value) {
        Tuple key = new Tuple(rowKey, column);
        if (twoKeyTable.containsKey(key)) {
            V v = twoKeyTable.get(key);
            value.merge(v);
        }
        twoKeyTable.put(key, value);
        return value;
    }

    @Override
    public Table<Long, V, Long, V, SlidingTimeWindowTable<V>> cloneEmpty() {
        return null;
    }

    @Override
    public Value<?> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return twoKeyTable.get(new Tuple(from, to));
    }

    @Override
    public SlidingTimeWindowTable<V> merge(SlidingTimeWindowTable<V> that) {
        that.twoKeyTable.forEach((tempTuple, otherValue) -> {
            V value = twoKeyTable.get(tempTuple);
            if (value == null) {
                twoKeyTable.put(tempTuple, otherValue);
            } else {
                value.merge(otherValue);
            }
        });
        return this;
    }

    @Override
    public SlidingTimeWindowTable<V> fastClone() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return twoKeyTable.isEmpty();
    }

}
