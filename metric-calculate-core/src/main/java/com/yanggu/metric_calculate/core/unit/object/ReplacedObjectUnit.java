package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@MergeType("REPLACEDOBJECT")
@Objective(useCompareField = false, retainObject = true)
public class ReplacedObjectUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, ReplacedObjectUnit<T>>, Value<T>, Serializable {

    public T value;

    public ReplacedObjectUnit(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * merge object to this.
     *
     * @param that param
     * @return MergedUnit Object
     */
    @Override
    public ReplacedObjectUnit<T> merge(ReplacedObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.value = that.value;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s {object=%s}", getClass().getSimpleName(), this.value);
    }

    @Override
    public ReplacedObjectUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public T value() {
        return this.value;
    }

    /**
     * fastClone.
     *
     * @return ReplacedUnit
     */
    @Override
    public ReplacedObjectUnit fastClone() {
        return new ReplacedObjectUnit<>(value.fastClone());
    }

    /**
     * Equal or Not.
     *
     * @param that param
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
        ReplacedObjectUnit thatUnit = (ReplacedObjectUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
