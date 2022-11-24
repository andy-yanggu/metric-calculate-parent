/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

import java.io.Serializable;

public class OccupiedUnit<T extends Cloneable<T>> implements ObjectiveUnit<T, OccupiedUnit<T>>, Value<T>, Serializable {
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
        OccupiedUnit<T> occupiedUnit = new OccupiedUnit(value.fastClone());
        return occupiedUnit;
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
