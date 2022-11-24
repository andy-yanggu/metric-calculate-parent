/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.value;

public class ImmutableValue implements Value, Comparable<Value> {

    private final Comparable value;

    public ImmutableValue(Comparable value) {
        this.value = value;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public int compareTo(Value o) {
        return value.compareTo(o.value());
    }
}
