/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

public abstract class CompareUnit<K extends Comparable<K>, V, U extends CompareUnit<K, V, U>>
        extends KeyValueUnit<K, V, U> {

    public CompareUnit(K key, V value) {
        super(key, value);
    }
}
