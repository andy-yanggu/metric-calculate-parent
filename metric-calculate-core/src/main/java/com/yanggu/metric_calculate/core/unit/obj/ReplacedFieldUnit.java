package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.io.Serializable;

@MergeType("REPLACEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class ReplacedFieldUnit<T> implements ObjectiveUnit<T, ReplacedFieldUnit<T>>, Value<T>, Serializable {
    private static final long serialVersionUID = 5240954691662766328L;

    public T value;

    public ReplacedFieldUnit() {
    }

    public ReplacedFieldUnit(T value) {
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
     * @param that param
     * @return MergedUnit Object
     */
    @Override
    public ReplacedFieldUnit<T> merge(ReplacedFieldUnit<T> that) {
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
    public ReplacedFieldUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public T value() {
        return this.value;
    }

    /**
     * fastClone.
     * @return ReplacedUnit
     */
    @Override
    public ReplacedFieldUnit fastClone() {
        ReplacedFieldUnit replacedObjectUnit = new ReplacedFieldUnit(
            (this.value instanceof Cloneable2) ? ((Cloneable2) this.value).fastClone() : this.value);
        return replacedObjectUnit;
    }

    /**
     * Equal or Not.
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
        ReplacedFieldUnit thatUnit = (ReplacedFieldUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
