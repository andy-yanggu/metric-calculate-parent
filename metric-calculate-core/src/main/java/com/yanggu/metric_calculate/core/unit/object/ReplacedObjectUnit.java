package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

@NoArgsConstructor
@Objective(useCompareField = false, retainObject = true)
@MergeType(value = "REPLACEDOBJECT", timeWindowType = TIME_SLIDING_WINDOW)
public class ReplacedObjectUnit<T extends Clone<T>> implements ObjectiveUnit<T, ReplacedObjectUnit<T>>, Value<T>, Serializable {

    protected T valueData;

    public ReplacedObjectUnit(T valueData) {
        this.valueData = valueData;
    }

    public T getValueData() {
        return this.valueData;
    }

    public void setValueData(T valueData) {
        this.valueData = valueData;
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
        this.valueData = that.valueData;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s {object=%s}", getClass().getSimpleName(), this.valueData);
    }

    @Override
    public ReplacedObjectUnit<T> value(T object) {
        setValueData(object);
        return this;
    }

    @Override
    public T value() {
        return this.valueData;
    }

    /**
     * fastClone.
     *
     * @return ReplacedUnit
     */
    @Override
    public ReplacedObjectUnit<T> fastClone() {
        return new ReplacedObjectUnit<>(valueData.fastClone());
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
        if (this.valueData == null) {
            return thatUnit.valueData == null;
        } else {
            return this.valueData.equals(thatUnit.valueData);
        }
    }
}
