package com.yanggu.metric_calculate.core.unit.object;

import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@MergeType(value = "MAXFIELD", useParam = true)
@Objective(useCompareField = true, retainObject = false)
public class MaxFieldUnit<T extends Comparable<T> & Clone<T>> implements
        ObjectiveUnit<T, MaxFieldUnit<T>>, Value<Object> {

    private MaxObjectUnit<T> maxObjectUnit = new MaxObjectUnit<>();

    public MaxFieldUnit(Map<String, Object> params) {
        this.maxObjectUnit = new MaxObjectUnit<>(params);
    }

    public MaxFieldUnit(T o) {
        maxObjectUnit.setValue(o);
    }

    @Override
    public MaxFieldUnit<T> merge(MaxFieldUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.maxObjectUnit.merge(that.maxObjectUnit);
        return this;
    }

    @Override
    public MaxFieldUnit<T> fastClone() {
        MaxFieldUnit<T> maxFieldUnit = new MaxFieldUnit<>();
        maxFieldUnit.maxObjectUnit = maxObjectUnit.fastClone();
        return maxFieldUnit;
    }

    @Override
    public MaxFieldUnit<T> value(T object) {
        maxObjectUnit.value(object);
        return this;
    }

    @Override
    public Object value() {
        return maxObjectUnit.value();
    }

    public T getValue() {
        return this.maxObjectUnit.getValue();
    }

    public void setValue(T value) {
        this.maxObjectUnit.setValue(value);
    }

    public Boolean getOnlyShowValue() {
        return this.maxObjectUnit.getOnlyShowValue();
    }

    public void setOnlyShowValue(Boolean onlyShowValue) {
        this.maxObjectUnit.setOnlyShowValue(onlyShowValue);
    }

    @Override
    public String toString() {
        return "MaxFieldUnit{" +
                "maxObjectUnit=" + maxObjectUnit +
                '}';
    }

    @Override
    public int hashCode() {
        return maxObjectUnit != null ? maxObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaxFieldUnit<?> that = (MaxFieldUnit<?>) o;

        return Objects.equals(maxObjectUnit, that.maxObjectUnit);
    }

}