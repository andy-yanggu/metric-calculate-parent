package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Objective(useCompareField = false, retainObject = true)
@MergeType(value = "OCCUPIEDOBJECT", timeWindowType = TIME_SLIDING_WINDOW)
public class OccupiedObjectUnit<T extends Cloneable2<T>> implements ObjectiveUnit<T, OccupiedObjectUnit<T>>,
        Value<T>, Serializable {

    protected T value;

    public OccupiedObjectUnit(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
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
        return new OccupiedObjectUnit<>(value.fastClone());
    }

    @Override
    public String toString() {
        return String.format("%s {object=%s}", getClass().getSimpleName(), this.value);
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
