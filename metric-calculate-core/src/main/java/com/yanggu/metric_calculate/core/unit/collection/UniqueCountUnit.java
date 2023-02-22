package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@MergeType(value = "DISTINCTCOUNT", useParam = true)
@Collective(useDistinctField = true, retainObject = true)
public class UniqueCountUnit<T extends Cloneable2<T>> implements CollectionUnit<T, UniqueCountUnit<T>>,
        Value<Number> {

    private DistinctListObjectUnit<T> original;

    public UniqueCountUnit() {
        original = new DistinctListObjectUnit<>();
    }

    public UniqueCountUnit(Map<String, Object> param) {
        original = new DistinctListObjectUnit<>(param);
    }

    public UniqueCountUnit(T value) {
        original = new DistinctListObjectUnit<>(value);
    }

    public UniqueCountUnit(Collection<T> values) {
        original = new DistinctListObjectUnit<>(values);
    }

    /**
     * Construct.
     *
     * @param value input param
     * @param limit limitCnt
     */
    public UniqueCountUnit(T value, int limit) {
        original = new DistinctListObjectUnit<>(value, limit);
    }

    /**
     * Construct.
     */
    public UniqueCountUnit(Collection<T> values, int limit) {
        original = new DistinctListObjectUnit<>(values, limit);
    }

    public DistinctListObjectUnit<T> original() {
        return original;
    }

    @Override
    public UniqueCountUnit<T> merge(UniqueCountUnit<T> that) {
        original.merge(that.original);
        return this;
    }

    @Override
    public Number value() {
        return original.asCollection().size();
    }

    public Collection<T> asCollection() {
        return this.original.asCollection();
    }

    /**
     * add value to hash set.
     *
     * @param value input param
     * @return
     */
    @Override
    public UniqueCountUnit<T> add(T value) {
        this.original.add(value);
        return this;
    }

    /**
     * add values to hash set.
     */
    public void addAll(Collection<T> values) {
        this.original.addAll(values);
    }

    @Override
    public String toString() {
        return original.toString();
    }

    /**
     * fast clone object.
     *
     * @return UniqueListUnit
     */
    @Override
    public UniqueCountUnit<T> fastClone() {
        UniqueCountUnit<T> result = new UniqueCountUnit<>();
        result.original = this.original;
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(original);
    }

    /**
     * isEqual or Not.
     *
     * @param that input Args
     *
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
        UniqueCountUnit<T> thatUnit = (UniqueCountUnit) that;
        if (this.original == null) {
            return thatUnit.original == null;
        } else {
            return this.original.equals(thatUnit.original);
        }
    }

}
