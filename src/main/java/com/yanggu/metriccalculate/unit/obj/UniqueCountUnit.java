/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.annotation.Collective;
import com.yanggu.metriccalculate.unit.CollectionUnit;
import com.yanggu.metriccalculate.unit.UnlimitedMergedUnit;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Moved {@link com.yanggu.metriccalculate.unit.collection.UniqueCountUnit}.
 */
@Collective
@Deprecated
public class UniqueCountUnit<T extends Cloneable<T>> implements CollectionUnit<T, UniqueCountUnit<T>>,
        UnlimitedMergedUnit<UniqueCountUnit<T>>, Value<Number>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -5104878154756554088L;

    private UniqueListUnit<T> original;

    public UniqueCountUnit() {
        original = new UniqueListUnit<>();
    }

    public UniqueCountUnit(T value) {
        original = new UniqueListUnit<>(value);
    }

    public UniqueCountUnit(Collection<T> values) {
        original = new UniqueListUnit<>(values);
    }

    /**
     * Construct.
     *
     * @param value input param
     * @param limit limitCnt
     */
    public UniqueCountUnit(T value, int limit) {
        original = new UniqueListUnit<>(value, limit);
    }

    /**
     * Construct.
     */
    public UniqueCountUnit(Collection<T> values, int limit) {
        original = new UniqueListUnit<>(values, limit);
    }

    public UniqueListUnit<T> original() {
        return original;
    }

    public UniqueCountUnit<T> merge(com.yanggu.metriccalculate.unit.collection.UniqueCountUnit<T> that) {
        original.merge(that.original());
        return this;
    }

    @Override
    public UniqueCountUnit<T> merge(UniqueCountUnit<T> that) {
        original.merge(that.original);
        return this;
    }

    public UniqueCountUnit<T> unlimitedMerge(com.yanggu.metriccalculate.unit.collection.UniqueCountUnit<T> that) {
        original.unlimitedMerge(that.original());
        return this;
    }

    @Override
    public UniqueCountUnit<T> unlimitedMerge(UniqueCountUnit<T> that) {
        original.unlimitedMerge(that.original);
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
    public Iterator<T> iterator() {
        return this.original.iterator();
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

    public Set<T> getSet() {
        return this.original.getSet();
    }

    public void setSet(Set<T> originalSet) {
        this.original.setSet(originalSet);
    }

}
