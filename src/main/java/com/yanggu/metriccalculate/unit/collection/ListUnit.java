package com.yanggu.metriccalculate.unit.collection;


import com.yanggu.metriccalculate.annotation.Collective;
import com.yanggu.metriccalculate.unit.CollectionUnit;
import com.yanggu.metriccalculate.unit.UnlimitedMergedUnit;
import com.yanggu.metriccalculate.value.Value;
import com.yanggu.metriccalculate.value.Cloneable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Collective
public class ListUnit<T extends Cloneable<T>> implements UnlimitedMergedUnit<ListUnit<T>>,
        CollectionUnit<T, ListUnit<T>>, Value<List<T>>, Serializable, Iterable<T> {
    private static final long serialVersionUID = -1300607404480893613L;

    private List<T> values = new ArrayList<>();

    public int limit = 0;

    public ListUnit() {}

    public ListUnit(T value) {
        this();
        add(value);
    }

    /**
     * Construct.
     */
    public ListUnit(T value, int limit) {
        this();
        add(value);
        this.limit = limit;
    }

    public List<T> getList() {
        return this.values;
    }

    public void setList(List<T> paramList) {
        this.values = paramList;
    }

    /**
     * add.
     * @return
     */
    @Override
    public ListUnit<T> add(T value) {
        this.values.add(value);
        if (this.limit > 0 && this.values.size() > this.limit) {
            this.values.remove(0);
        }
        return this;
    }

    public List<T> asList() {
        return this.values;
    }

    @Deprecated
    public ListUnit<T> merge(com.yanggu.metriccalculate.unit.obj.ListUnit<T> that) {
        return merge(that, false);
    }

    @Deprecated
    private ListUnit<T> merge(com.yanggu.metriccalculate.unit.obj.ListUnit<T> that, boolean useLimit) {
        if (that == null) {
            return this;
        }
        List<T> values = that.getList();
        int limit = that.limit;
        return internalMerge(values, useLimit, limit);
    }

    @Override
    public ListUnit<T> merge(ListUnit<T> that) {
        return merge(that, false);
    }

    private ListUnit<T> merge(ListUnit<T> that, boolean useLimit) {
        if (that == null) {
            return this;
        }
        List<T> values = that.values;
        int limit = that.limit;
        return internalMerge(values, useLimit, limit);
    }

    private ListUnit<T> internalMerge(List<T> values, boolean useLimit, int limit) {
        this.values.addAll(values);
        if (!useLimit) {
            this.limit = Math.max(this.limit, limit);
            int i = this.values.size();
            if (this.limit > 0 && i > this.limit) {
                List<T> list = this.values.subList(i - this.limit, i);
                this.values = new ArrayList<>(this.limit);
                this.values.addAll(list);
            }
        }
        return this;
    }

    @Override
    public ListUnit<T> unlimitedMerge(ListUnit<T> that) {
        return merge(that, true);
    }

    @Override
    public Iterator<T> iterator() {
        return this.values.iterator();
    }

    @Override
    public List<T> value() {
        return this.values;
    }

    @Override
    public ListUnit<T> fastClone() {
        ListUnit<T> mergeableListObject = new ListUnit<>();
        mergeableListObject.limit = this.limit;
        for (T item : getList()) {
            mergeableListObject.getList().add(item.fastClone());
        }
        return mergeableListObject;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        ListUnit<T> thatUnit = (ListUnit)that;
        if (this.values == null) {
            if (thatUnit.values != null) {
                return false;
            }
        } else if (!this.values.equals(thatUnit.values)) {
            return false;
        }
        return this.limit == thatUnit.limit;
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, list=%s}", this.limit, this.values.toString());
    }

}
