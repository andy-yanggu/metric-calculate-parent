package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.unit.MergedUnit;

public abstract class KeyValueUnit<K extends Comparable<K>, V, U extends KeyValueUnit<K, V, U>>
        implements MergedUnit<U> {

    private K key;

    private V value;

    public KeyValueUnit(K key, V value) {
        this.key = key;
        this.value = value;
    }

}
