package com.yanggu.metric_calculate.core.unit.obj;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.value.*;

import java.util.Map;

@MergeType(value = "MAXOBJECT", useParam = true)
@Objective(useCompareField = true, retainObject = true)
public class MaxObjectUnit<T extends Comparable<T> & Cloneable2<T>> implements ObjectiveUnit<T, MaxObjectUnit<T>>, Value {
    private T maxValue;

    /**
     * 是否只展示value, 不展示key
     */
    private boolean onlyShowValue;

    public MaxObjectUnit() {
    }

    public MaxObjectUnit(Map<String, Object> params) {
        this.onlyShowValue = (boolean) params.get("onlyShowValue");
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
        } else if (this.maxValue instanceof KeyValue && onlyShowValue) {
            Cloneable2 value = ((KeyValue) maxValue).getValue();
            if (value != null) {
                return ValueMapper.value(((Value<?>) value));
            }
        } else if (this.maxValue instanceof Value) {
            return ValueMapper.value(((Value<?>) maxValue));
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