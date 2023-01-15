package com.yanggu.metric_calculate.core.unit.object;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Map;

@Data
@FieldNameConstants
@MergeType(value = "MINFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MinFieldUnit<T extends Comparable<T> & Cloneable2<T>> implements ObjectiveUnit<T, MinFieldUnit<T>>, Value {

    private T value;

    /**
     * 是否只展示value, 不展示key
     */
    private Boolean onlyShowValue = true;

    public MinFieldUnit() {
    }

    public MinFieldUnit(T value) {
        setValue(value);
    }

    public MinFieldUnit(Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return;
        }
        Object tempShowValue = params.get(Fields.onlyShowValue);
        if (tempShowValue instanceof Boolean) {
            this.onlyShowValue = (Boolean) tempShowValue;
        }
    }

    @Override
    public MinFieldUnit<T> merge(MinFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.value.compareTo(that.getValue()) >= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    @Override
    public MinFieldUnit<T> fastClone() {
        return new MinFieldUnit<>(value.fastClone());
    }

    @Override
    public MinFieldUnit<T> value(T object) {
        setValue(object);
        return this;
    }

    @Override
    public Object value() {
        if (this.value == null) {
            return null;
        } else if (this.value instanceof KeyValue && Boolean.TRUE.equals(onlyShowValue)) {
            Value<?> tempValue = ((KeyValue<?, ?>) this.value).getValue();
            return ValueMapper.value(tempValue);
        } else if (this.value instanceof Value) {
            return ValueMapper.value(((Value<?>) value));
        }
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("%s {value=%s}", getClass().getSimpleName(), this.value);
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
        MinFieldUnit<T> thatUnit = (MinFieldUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }

}
