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
@MergeType(value = "MINOBJECT", useParam = true)
@Objective(useCompareField = true, retainObject = true)
public class MinObjectUnit<T extends Comparable<T> & Clone<T>> implements ObjectiveUnit<T, MinObjectUnit<T>>, Value<Object> {

    protected T value;

    /**
     * 是否只展示value, 不展示key
     */
    protected Boolean onlyShowValue = true;

    public MinObjectUnit() {
    }

    public MinObjectUnit(Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return;
        }
        Object tempShowValue = params.get(Fields.onlyShowValue);
        if (tempShowValue instanceof Boolean) {
            this.onlyShowValue = (Boolean) tempShowValue;
        }
    }

    public MinObjectUnit(T value) {
        setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public MinObjectUnit<T> merge(MinObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        if (that.getValue() != null && this.value.compareTo(that.getValue()) >= 0) {
            setValue(that.getValue());
        }
        return this;
    }

    @Override
    public MinObjectUnit<T> fastClone() {
        return new MinObjectUnit<>(value.fastClone());
    }

    @Override
    public MinObjectUnit<T> value(T object) {
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
        MinObjectUnit<T> thatUnit = (MinObjectUnit) that;
        if (this.value == null) {
            return thatUnit.value == null;
        } else {
            return this.value.equals(thatUnit.value);
        }
    }

}
