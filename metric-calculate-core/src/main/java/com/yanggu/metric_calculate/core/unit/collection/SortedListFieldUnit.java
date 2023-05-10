package com.yanggu.metric_calculate.core.unit.collection;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@MergeType(value = "SORTEDLIMITLISTFIELD", useParam = true)
@Collective(useSortedField = true, retainObject = false)
public class SortedListFieldUnit<T extends Comparable<T> & Clone<T>>
        implements CollectionUnit<T, SortedListFieldUnit<T>>, Value<List<Object>> {

    private SortedListObjectUnit<T> sortedListObjectUnit;

    public SortedListFieldUnit() {
        this.sortedListObjectUnit = new SortedListObjectUnit<>();
    }

    public SortedListFieldUnit(Map<String, Object> params) {
        this.sortedListObjectUnit = new SortedListObjectUnit<>(params);
    }

    /**
     * Constructor.
     * @param value  value
     * @param limit list limit
     */
    public SortedListFieldUnit(T value, int limit) {
        this();
        this.sortedListObjectUnit.limit = limit;
        add(value);
    }

    @Override
    public SortedListFieldUnit<T> add(T value) {
        this.sortedListObjectUnit.add(value);
        return this;
    }

    @Override
    public SortedListFieldUnit<T> merge(SortedListFieldUnit<T> that) {
        this.sortedListObjectUnit.merge(that.sortedListObjectUnit);
        return this;
    }

    @Override
    public SortedListFieldUnit<T> fastClone() {
        SortedListFieldUnit<T> mergeableSortedList = new SortedListFieldUnit<>();
        mergeableSortedList.sortedListObjectUnit = this.sortedListObjectUnit.fastClone();
        return mergeableSortedList;
    }

    @Override
    public List<Object> value() {
        return this.sortedListObjectUnit.value();
    }

    @Override
    public int hashCode() {
        return sortedListObjectUnit != null ? sortedListObjectUnit.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SortedListFieldUnit<?> that = (SortedListFieldUnit<?>) o;
        return Objects.equals(sortedListObjectUnit, that.sortedListObjectUnit);
    }

}
