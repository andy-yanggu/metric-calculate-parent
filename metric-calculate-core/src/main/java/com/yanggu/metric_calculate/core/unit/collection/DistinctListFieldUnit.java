package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@MergeType(value = "DISTINCTLISTFIELD", useParam = true)
@Collective(useDistinctField = true, retainObject = false)
public class DistinctListFieldUnit<T extends Cloneable2<T>> implements CollectionUnit<T, DistinctListFieldUnit<T>>,
        Value<Set<Object>> {

    private DistinctListObjectUnit<T> distinctListObjectUnit;

    public DistinctListFieldUnit() {
        this.distinctListObjectUnit = new DistinctListObjectUnit<>();
    }

    public DistinctListFieldUnit(Map<String, Object> param) {
        this.distinctListObjectUnit = new DistinctListObjectUnit<>(param);
    }

    public DistinctListFieldUnit(T value) {
        this();
        add(value);
    }

    /**
     * Construct.
     *
     * @param value input
     * @param limit limitCnt
     */
    public DistinctListFieldUnit(T value, int limit) {
        this();
        add(value);
        this.distinctListObjectUnit.limit = limit;
    }

    @Override
    public DistinctListFieldUnit<T> add(T value) {
        this.distinctListObjectUnit.add(value);
        return this;
    }

    @Override
    public DistinctListFieldUnit<T> merge(DistinctListFieldUnit<T> that) {
        this.distinctListObjectUnit.merge(that.distinctListObjectUnit);
        return this;
    }

    @Override
    public DistinctListFieldUnit<T> fastClone() {
        DistinctListFieldUnit<T> distinctListFieldUnit = new DistinctListFieldUnit<>();
        distinctListFieldUnit.distinctListObjectUnit = this.distinctListObjectUnit.fastClone();
        return distinctListFieldUnit;
    }

    @Override
    public Set<Object> value() {
        return this.distinctListObjectUnit.value();
    }

    @Override
    public int hashCode() {
        return distinctListObjectUnit != null ? distinctListObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DistinctListFieldUnit<?> that = (DistinctListFieldUnit<?>) o;
        return Objects.equals(distinctListObjectUnit, that.distinctListObjectUnit);
    }

}
