package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.io.Serializable;

@Data
@MergeType("OCCUPIEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class OccupiedFieldUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, OccupiedFieldUnit<T>>, Value<T>, Serializable {

    private static final long serialVersionUID = -617729814303380664L;

    public T value;

    public OccupiedFieldUnit() {
    }

    public OccupiedFieldUnit(T value) {
        this.value = value;
    }

    @Override
    public OccupiedFieldUnit<T> merge(OccupiedFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (this.value == null) {
            this.value = that.value;
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s {object=%s}", getClass().getSimpleName(), this.value);
    }

    @Override
    public T value() {
        return this.value;
    }

    @Override
    public OccupiedFieldUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public OccupiedFieldUnit<T> fastClone() {
        return (OccupiedFieldUnit<T>) new OccupiedFieldUnit(value.fastClone());
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
        OccupiedFieldUnit<T> thatUnit = (OccupiedFieldUnit) that;
        if (this.value == null) {
            if (thatUnit.value != null) {
                return false;
            }
        } else if (!this.value.equals(thatUnit.value)) {
            return false;
        }
        return true;
    }

}
