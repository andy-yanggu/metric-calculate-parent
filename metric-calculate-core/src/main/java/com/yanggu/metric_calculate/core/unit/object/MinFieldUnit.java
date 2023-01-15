package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;

@MergeType("MINFIELD")
@Objective(useCompareField = true, retainObject = false)
public class MinFieldUnit<T extends Comparable<T> & Cloneable2<T>> implements ObjectiveUnit<T, MinFieldUnit<T>>, Value {
    private T value;

    public MinFieldUnit() {
    }

    public MinFieldUnit(T value) {
        setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public MinFieldUnit<T> merge(MinFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.value.compareTo(that.getValue()) >= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    @Override
    public MinFieldUnit<T> fastClone() {
        MinFieldUnit<T> minObjectUnit = new MinFieldUnit<>(value.fastClone());
        return minObjectUnit;
    }

    @Override
    public MinFieldUnit<T> value(T object) {
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
        MinFieldUnit<T> thatUnit = (MinFieldUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
