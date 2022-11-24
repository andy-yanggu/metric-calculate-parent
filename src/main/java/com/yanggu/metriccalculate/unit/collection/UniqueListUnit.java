/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.collection;

import com.yanggu.metriccalculate.annotation.Collective;
import com.yanggu.metriccalculate.unit.CollectionUnit;
import com.yanggu.metriccalculate.unit.UnlimitedMergedUnit;
import com.yanggu.metriccalculate.value.Value;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.yanggu.metriccalculate.value.Cloneable;

@Collective
public class UniqueListUnit<T extends Cloneable<T>> implements CollectionUnit<T, UniqueListUnit<T>>,
        UnlimitedMergedUnit<UniqueListUnit<T>>, Value<Collection<T>>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -5104878154756554088L;

    private Set<T> original = new HashSet<>();

    private int limit = 0;

    public UniqueListUnit() {
    }

    public UniqueListUnit(T value) {
        this();
        add(value);
    }

    public UniqueListUnit(Collection<T> values) {
        this();
        addAll(values);
    }

    /**
     * Construct.
     *
     * @param value input
     * @param limit    limitCnt
     */
    public UniqueListUnit(T value, int limit) {
        this();
        add(value);
        this.limit = limit;
    }

    /**
     * Construct.
     */
    public UniqueListUnit(Collection<T> values, int paramCnt) {
        this();
        addAll(values);
        this.limit = paramCnt;
    }

    public Set<T> original() {
        return original;
    }

    public int limit() {
        return limit;
    }

    @Deprecated
    public UniqueListUnit<T> merge(com.yanggu.metriccalculate.unit.obj.UniqueListUnit<T> that) {
        return internalMergeOp(that, false);
    }

    @Override
    public UniqueListUnit<T> merge(UniqueListUnit<T> that) {
        return internalMergeOp(that, false);
    }

    @Deprecated
    public UniqueListUnit<T> unlimitedMerge(com.yanggu.metriccalculate.unit.obj.UniqueListUnit<T> that) {
        return internalMergeOp(that, true);
    }

    @Override
    public UniqueListUnit<T> unlimitedMerge(UniqueListUnit<T> that) {
        return internalMergeOp(that, true);
    }

    @Deprecated
    private UniqueListUnit<T> internalMergeOp(
        com.yanggu.metriccalculate.unit.obj.UniqueListUnit<T> that,
        boolean hasLimit
    ) {
        if (that == null) {
            return this;
        }
        return originalMerge(that.original(), that.limit(), hasLimit);
    }

    private UniqueListUnit<T> internalMergeOp(UniqueListUnit<T> that, boolean hasLimit) {
        if (that == null) {
            return this;
        }
        return originalMerge(that.original, that.limit, hasLimit);
    }

    private UniqueListUnit<T> originalMerge(Set<T> original, int limit, boolean hasLimit) {
        this.original.addAll(original);
        if (!hasLimit) {
            this.limit = Math.max(this.limit, limit);
            int i = this.original.size();
            if (this.limit > 0 && i > this.limit) {
                byte b = 0;
                HashSet<T> hashSet = new HashSet<>();
                int j = i - this.limit;
                for (T item : this.original) {
                    if (b++ < j) {
                        continue;
                    }
                    hashSet.add(item);
                }
                this.original = hashSet;
            }
        }
        return this;
    }

    @Override
    public Collection<T> value() {
        return this.original;
    }

    public Collection<T> asCollection() {
        return this.original;
    }

    /**
     * Add value to original.
     * @param value input param
     * @return
     */
    @Override
    public UniqueListUnit<T> add(T value) {
        this.original.add(value);
        if (this.limit > 0 && this.original.size() > this.limit) {
            this.original.remove(this.original.iterator().next());
        }
        return this;
    }

    /**
     * Add values to original.
     */
    public void addAll(Collection<T> values) {
        this.original.addAll(values);
        while (this.limit > 0 && this.original.size() > this.limit) {
            this.original.remove(this.original.iterator().next());
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this.original.iterator();
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, set=%s}", this.limit, this.original.toString());
    }

    @Override
    public UniqueListUnit<T> fastClone() {
        UniqueListUnit<T> uniqueListUnit = new UniqueListUnit<>();
        uniqueListUnit.limit = this.limit;
        for (T item : getSet()) {
            uniqueListUnit.getSet().add(item.fastClone());
        }
        return uniqueListUnit;
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
        UniqueListUnit<T> thatUnit = (UniqueListUnit) that;
        if (this.limit != thatUnit.limit) {
            return false;
        }
        if (this.original == null) {
            return thatUnit.original == null;
        } else {
            return this.original.equals(thatUnit.original);
        }
    }

    public Set<T> getSet() {
        return this.original;
    }

    public void setSet(Set<T> distinctHashRs) {
        this.original = distinctHashRs;
    }

}
