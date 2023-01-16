package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.Data;

import java.io.Serializable;

@Data
@Objective(useCompareField = false, retainObject = true)
@MergeType("OCCUPIEDOBJECT")
public class OccupiedObjectUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, OccupiedObjectUnit<T>>, Value<T>, Serializable {

    private static final long serialVersionUID = -617729814303380664L;

    public T value;

    public OccupiedObjectUnit() {
    }

    public OccupiedObjectUnit(T value) {
        this.value = value;
    }

    @Override
    public OccupiedObjectUnit<T> merge(OccupiedObjectUnit<T> that) {
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
    public OccupiedObjectUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public OccupiedObjectUnit<T> fastClone() {
        return (OccupiedObjectUnit<T>) new OccupiedObjectUnit(value.fastClone());
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
        OccupiedObjectUnit<T> thatUnit = (OccupiedObjectUnit) that;
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
