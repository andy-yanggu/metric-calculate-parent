package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Cloneable;

@MergeType("MAXOBJECT")
@Objective
public class MaxObjectUnit<T extends Comparable<T> & Cloneable<T>> implements ObjectiveUnit<T, MaxObjectUnit<T>>, Value {
    private T maxValue;

    public MaxObjectUnit() {
    }

    public MaxObjectUnit(T o) {
        setValue(o);
    }

    public void setValue(T value) {
        this.maxValue = value;
    }

    public T getValue() {
        return this.maxValue;
    }

    @Override
    public MaxObjectUnit<T> merge(MaxObjectUnit<T> that) {
        return maxValue(that);
    }

    private MaxObjectUnit<T> maxValue(MaxObjectUnit<T> that) {
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
    public MaxObjectUnit<T> fastClone() {
        MaxObjectUnit<T> maxUnit = new MaxObjectUnit<>(this.maxValue.fastClone());
        return maxUnit;
    }

    @Override
    public MaxObjectUnit<T> value(T object) {
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
        MaxObjectUnit<T> thatUnit = (MaxObjectUnit) that;
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