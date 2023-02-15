package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@MergeType(value = "SORTEDLISTFIELD", useParam = true)
@Collective(useSortedField = true, retainObject = false)
public class SortedListFieldUnit<T extends Comparable<T> & Cloneable2<T>> extends SortedListObjectUnit<T> {

    public SortedListFieldUnit(Map<String, Object> params) {
        super(params);
    }

    public SortedListFieldUnit(T value) {
        this(value, 0, true);
    }

    /**
     * Constructor.
     * @param value  value
     * @param limit list limit
     * @param desc des or not
     */
    public SortedListFieldUnit(T value, int limit, boolean desc) {
        this();
        this.limit = limit;
        add(value);
    }

    public SortedListFieldUnit(T value, boolean desc) {
        this(value, 0, desc);
    }

    public SortedListFieldUnit(T value, int limit) {
        this(value, limit, true);
    }

    @Override
    public SortedListFieldUnit<T> fastClone() {
        SortedListFieldUnit<T> mergeableSortedList = new SortedListFieldUnit<>();
        mergeableSortedList.limit = this.limit;
        mergeableSortedList.boundedPriorityQueue = new BoundedPriorityQueue<>(this.limit);

        for (T item : getList()) {
            mergeableSortedList.add(item);
        }
        return mergeableSortedList;
    }

}
