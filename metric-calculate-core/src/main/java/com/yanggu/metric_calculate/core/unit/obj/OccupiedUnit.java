package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Cloneable2;

import java.io.Serializable;

@Objective
@MergeType("OCCUPIED")
public class OccupiedUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, OccupiedUnit<T>>, Value<T>, Serializable {

    private static final long serialVersionUID = -617729814303380664L;

    public T value;

    public OccupiedUnit() {
    }

    public OccupiedUnit(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public OccupiedUnit<T> merge(OccupiedUnit<T> that) {
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
    public OccupiedUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public OccupiedUnit<T> fastClone() {
        return (OccupiedUnit<T>) new OccupiedUnit(value.fastClone());
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
        OccupiedUnit<T> thatUnit = (OccupiedUnit) that;
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
