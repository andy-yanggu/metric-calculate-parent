package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.Collection;
import java.util.Map;

@MergeType(value = "DISTINCTCOUNT", useParam = true)
@Collective(useCompareField = true, retainObject = true)
public class UniqueCountUnit<T extends Cloneable2<T>> implements CollectionUnit<T, UniqueCountUnit<T>>,
        Value<Number> {

    private UniqueListObjectUnit<T> original;

    public UniqueCountUnit() {
        original = new UniqueListObjectUnit<>();
    }

    public UniqueCountUnit(Map<String, Object> param) {
        original = new UniqueListObjectUnit<>(param);
    }

    public UniqueCountUnit(T value) {
        original = new UniqueListObjectUnit<>(value);
    }

    public UniqueCountUnit(Collection<T> values) {
        original = new UniqueListObjectUnit<>(values);
    }

    /**
     * Construct.
     *
     * @param value input param
     * @param limit limitCnt
     */
    public UniqueCountUnit(T value, int limit) {
        original = new UniqueListObjectUnit<>(value, limit);
    }

    /**
     * Construct.
     */
    public UniqueCountUnit(Collection<T> values, int limit) {
        original = new UniqueListObjectUnit<>(values, limit);
    }

    public UniqueListObjectUnit<T> original() {
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
