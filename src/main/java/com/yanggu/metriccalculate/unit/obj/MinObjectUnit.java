package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.annotation.Objective;
import com.yanggu.metriccalculate.value.NoneValue;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

@Objective
public class MinObjectUnit<T extends Comparable<T> & Cloneable<T>> implements ObjectiveUnit<T, MinObjectUnit<T>>, Value {
    private T value;

    public MinObjectUnit() {
    }

    public MinObjectUnit(T value) {
        setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public MinObjectUnit<T> merge(MinObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.value.compareTo(that.getValue()) >= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    @Override
    public MinObjectUnit<T> fastClone() {
        MinObjectUnit<T> minObjectUnit = new MinObjectUnit<>(value.fastClone());
        return minObjectUnit;
    }

    @Override
    public MinObjectUnit<T> value(T object) {
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
        MinObjectUnit<T> thatUnit = (MinObjectUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
