package com.yanggu.metric_calculate.core.unit.obj;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.value.*;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@FieldNameConstants
@MergeType(value = "MAXFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MaxFieldUnit<T extends Comparable<T> & Cloneable2<T>> implements ObjectiveUnit<T, MaxFieldUnit<T>>, Value {
    private T maxValue;

    /**
     * 是否只展示value, 不展示key
     */
    private boolean onlyShowValue = true;

    public MaxFieldUnit() {
    }

    public MaxFieldUnit(Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return;
        }
        Object tempShowValue = params.get(Fields.onlyShowValue);
        if (tempShowValue instanceof Boolean) {
            this.onlyShowValue = (boolean) tempShowValue;
        }
    }

    public MaxFieldUnit(T o) {
        setValue(o);
    }

    public void setValue(T value) {
        this.maxValue = value;
    }

    public T getValue() {
        return this.maxValue;
    }

    @Override
    public MaxFieldUnit<T> merge(MaxFieldUnit<T> that) {
        return maxValue(that);
    }

    private MaxFieldUnit<T> maxValue(MaxFieldUnit<T> that) {
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
    public MaxFieldUnit<T> fastClone() {
        MaxFieldUnit<T> maxUnit = new MaxFieldUnit<>(this.maxValue.fastClone());
        return maxUnit;
    }

    @Override
    public MaxFieldUnit<T> value(T object) {
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
        MaxFieldUnit<T> thatUnit = (MaxFieldUnit) that;
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