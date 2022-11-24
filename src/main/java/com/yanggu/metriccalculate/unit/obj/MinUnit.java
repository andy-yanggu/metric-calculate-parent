/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.annotation.Objective;
import com.yanggu.metriccalculate.value.NoneValue;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

@Objective
public class MinUnit<T extends Comparable<T> & Cloneable<T>> implements ObjectiveUnit<T, MinUnit<T>>, Value {
    private T value;

    public MinUnit() {
    }

    public MinUnit(T value) {
        setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public MinUnit<T> merge(MinUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.value.compareTo(that.getValue()) >= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    @Override
    public MinUnit<T> fastClone() {
        MinUnit<T> minUnit = new MinUnit<>(value.fastClone());
        return minUnit;
    }

    @Override
    public MinUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public Object value() {
        if (this.value == null) {
            return NoneValue.INSTANCE;
        }
        if (this.value instanceof Value) {
            return ((Value) this.value).value();
        }
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("%s {value=%s}", getClass().getSimpleName(), this.value);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        MinUnit<T> thatUnit = (MinUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
