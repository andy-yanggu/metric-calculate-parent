package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Map;
import java.util.Objects;

@Data
@FieldNameConstants
@MergeType(value = "MINFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MinFieldUnit<T extends Comparable<T> & Clone<T>>
        implements ObjectiveUnit<T, MinFieldUnit<T>>, Value<Object> {

    private MinObjectUnit<T> minObjectUnit;

    public MinFieldUnit() {
        this.minObjectUnit = new MinObjectUnit<>();
    }

    public MinFieldUnit(Map<String, Object> params) {
        this.minObjectUnit = new MinObjectUnit<>(params);
    }

    public MinFieldUnit(T value) {
        this();
        value(value);
    }

    @Override
    public MinFieldUnit<T> value(T object) {
        this.minObjectUnit.value(object);
        return this;
    }

    @Override
    public MinFieldUnit<T> merge(MinFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.minObjectUnit.merge(that.minObjectUnit);
        return this;
    }

    @Override
    public Object value() {
        return this.minObjectUnit.value();
    }

    @Override
    public MinFieldUnit<T> fastClone() {
        MinFieldUnit<T> minFieldUnit = new MinFieldUnit<>();
        minFieldUnit.minObjectUnit = this.minObjectUnit.fastClone();
        return minFieldUnit;
    }

    @Override
    public int hashCode() {
        return minObjectUnit != null ? minObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MinFieldUnit<?> that = (MinFieldUnit<?>) o;

        return Objects.equals(minObjectUnit, that.minObjectUnit);
    }

}
