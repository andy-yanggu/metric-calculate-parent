package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 和SQL中的ORDER BY 排序字段 DESC/ASC LIMIT 5语义一致
 *
 * @param <T>
 */

@FieldNameConstants
@MergeType(value = "SORTEDLISTOBJECT", useParam = true)
@Collective(useSortedField = true, retainObject = true)
public class SortedListObjectUnit<T extends Comparable<T> & Clone<T>> implements CollectionUnit<T, SortedListObjectUnit<T>>, Value<List<Object>> {

    /**
     * 是否只展示value, 不展示key
     */
    protected Boolean onlyShowValue = true;

    protected Integer limit = 10;

    /**
     * 有界有限队列
     */
    protected BoundedPriorityQueue<T> boundedPriorityQueue = new BoundedPriorityQueue<>(limit);

    public SortedListObjectUnit() {
    }

    public SortedListObjectUnit(Map<String, Object> params) {
        if (CollUtil.isEmpty(params)) {
            return;
        }
        Object tempShowValue = params.get(Fields.onlyShowValue);
        if (tempShowValue instanceof Boolean) {
            this.onlyShowValue = (boolean) tempShowValue;
        }
        Object tempLimit = params.get(Fields.limit);
        if (tempLimit instanceof Integer) {
            this.limit = (int) tempLimit;
        }
        this.boundedPriorityQueue = new BoundedPriorityQueue<>(this.limit);
    }

    public SortedListObjectUnit(T value) {
        this.boundedPriorityQueue = new BoundedPriorityQueue<>(this.limit);
        add(value);
    }

    public SortedListObjectUnit(T value, int limit) {
        this.boundedPriorityQueue = new BoundedPriorityQueue<>(limit);
        add(value);
    }

    public int limit() {
        return limit;
    }

    public List<T> getList() {
        return this.boundedPriorityQueue.toList();
    }

    /**
     * add element.
     *
     * @param value value
     * @return
     */
    @Override
    public SortedListObjectUnit<T> add(T value) {
        this.boundedPriorityQueue.offer(value);
        return this;
    }

    @Override
    public SortedListObjectUnit<T> merge(SortedListObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        List<T> list = that.getList();
        //如果limit发生变化, 取新的limit
        if (!that.limit.equals(this.limit)) {
            this.boundedPriorityQueue = new BoundedPriorityQueue<>(that.limit);
            list.addAll(this.getList());
        }
        list.forEach(this::add);
        return this;
    }

    @Override
    public SortedListObjectUnit<T> fastClone() {
        SortedListObjectUnit<T> mergeableSortedList = new SortedListObjectUnit<>();
        mergeableSortedList.limit = this.limit;
        mergeableSortedList.boundedPriorityQueue = new BoundedPriorityQueue<>(this.limit);
        for (T item : getList()) {
            mergeableSortedList.add(item);
        }
        return mergeableSortedList;
    }

    @Override
    public List<Object> value() {
        ArrayList<T> original = this.boundedPriorityQueue.toList();
        if (CollUtil.isEmpty(original)) {
            return Collections.emptyList();
        }
        if (original.get(0) instanceof KeyValue && Boolean.TRUE.equals(onlyShowValue)) {
            List<Object> returnList = new ArrayList<>(original.size());
            original.forEach(temp -> {
                Value<?> value = ((KeyValue<?, ?>) temp).getValue();
                if (value != null) {
                    returnList.add(ValueMapper.value(value));
                }
            });
            return returnList;
        }
        return ((List) original);
    }

    /**
     * IsEqual or Not.
     *
     * @param that param
     * @return ture or false
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
        SortedListObjectUnit<T> thatUnit = (SortedListObjectUnit) that;
        if (this.limit != thatUnit.limit) {
            return false;
        }
        if (this.boundedPriorityQueue == null) {
            return thatUnit.boundedPriorityQueue == null;
        } else {
            return this.boundedPriorityQueue.equals(thatUnit.boundedPriorityQueue);
        }
    }

    @Override
    public String toString() {
        return String.format("%s{limit=%s, list=%s}", getClass().getSimpleName(), limit, this.boundedPriorityQueue.toList());
    }

}
