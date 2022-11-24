/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.unit.MergedUnit;

public abstract class KeyValueUnit<K extends Comparable<K>, V, U extends KeyValueUnit<K, V, U>>
        implements MergedUnit<U> {

    private K key;
    private V value;

    public KeyValueUnit(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
