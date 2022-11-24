/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.annotation.Objective;
import com.yanggu.metriccalculate.value.NoneValue;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

@Objective
public class MaxUnit<T extends Comparable<T> & Cloneable<T>> implements ObjectiveUnit<T, MaxUnit<T>>, Value {
    private T maxValue;

    public MaxUnit() {
    }

    public MaxUnit(T o) {
        setValue(o);
    }

    public void setValue(T value) {
        this.maxValue = value;
    }

    public T getValue() {
        return this.maxValue;
    }

    @Override
    public MaxUnit<T> merge(MaxUnit<T> that) {
        return maxValue(that);
    }

    private MaxUnit<T> maxValue(MaxUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.maxValue.compareTo(that.getValue()) <= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    /**
     * FastClone.
     * @return MaxUnit
     */
    @Override
    public MaxUnit<T> fastClone() {
        MaxUnit<T> maxUnit = new MaxUnit<>(this.maxValue.fastClone());
        return maxUnit;
    }

    @Override
    public MaxUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    /**
     * getValue.
     * @return object value
     */
    @Override
    public Object value() {
        if (this.maxValue == null) {
            return NoneValue.INSTANCE;
        }
        if (this.maxValue instanceof Value) {
            return ((Value) this.maxValue).value();
        }
        return this.maxValue;
    }

    @Override
    public String toString() {
        return String.format("%s {value=%s}", getClass().getSimpleName(),
            maxValue instanceof Value ? maxValue : ((Value)maxValue).value());
    }

    /**
     * Equal or Not.
     * @param that paramObj
     * @return true or false
     */
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
        MaxUnit<T> thatUnit = (MaxUnit) that;
        if (this.maxValue == null) {
            if (thatUnit.maxValue != null) {
                return false;
            }
        } else if (!this.maxValue.equals(thatUnit.maxValue)) {
            return false;
        }
        return true;
    }
}