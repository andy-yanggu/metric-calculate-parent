package com.yanggu.metric_calculate.core.cube;

import cn.hutool.core.lang.Tuple;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.HashMap;
import java.util.Map;


public class CountWindowTable<V extends MergedUnit<V> & Value<?>> implements Table<Long, V, Long, V, CountWindowTable<V>> {

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
    public Table<Long, V, Long, V, CountWindowTable<V>> cloneEmpty() {
        return null;
    }

    @Override
    public Value query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return twoKeyTable.get(new Tuple(from, to));
    }

    @Override
    public CountWindowTable<V> merge(CountWindowTable<V> that) {
        that.twoKeyTable.forEach((tempTuple, value) -> twoKeyTable.merge(tempTuple, value, (k, v) -> v.merge(value)));
        return this;
    }

    @Override
    public CountWindowTable<V> fastClone() {
        return null;
    }

}
