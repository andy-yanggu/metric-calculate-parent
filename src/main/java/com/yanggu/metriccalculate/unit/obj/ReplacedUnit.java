/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.annotation.Objective;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

import java.io.Serializable;

@Objective
public class ReplacedUnit<T> implements ObjectiveUnit<T, ReplacedUnit<T>>, Value<T>, Serializable {
    private static final long serialVersionUID = 5240954691662766328L;

    public T value;

    public ReplacedUnit() {
    }

    public ReplacedUnit(T value) {
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
    public ReplacedUnit<T> merge(ReplacedUnit<T> that) {
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
    public ReplacedUnit<T> value(T object) {
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
    public ReplacedUnit fastClone() {
        ReplacedUnit replacedUnit = new ReplacedUnit(
            (this.value instanceof Cloneable) ? ((Cloneable) this.value).fastClone() : this.value);
        return replacedUnit;
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
        ReplacedUnit thatUnit = (ReplacedUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }
}
