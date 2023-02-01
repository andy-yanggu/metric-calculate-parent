package com.yanggu.metric_calculate.core.unit.collection;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@MergeType(value = "LISTOBJECT", useParam = true)
@Collective(useCompareField = false, retainObject = true)
public class ListObjectUnit<T extends Cloneable2<T>> implements CollectionUnit<T, ListObjectUnit<T>>, Value<List<T>> {

    private List<T> values = new ArrayList<>();

    private Integer limit = 0;

    public ListObjectUnit(Map<String, Object> param) {
        if (CollUtil.isEmpty(param)) {
            return;
        }
        Object tempLimit = param.get("limit");
        if (tempLimit instanceof Integer) {
            this.limit = (Integer) tempLimit;
        }
    }

    public ListObjectUnit(T value) {
        this();
        add(value);
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * add.
     *
     * @return
     */
    @Override
    public ListObjectUnit<T> add(T value) {
        this.values.add(value);
        if (this.limit > 0 && this.values.size() > this.limit) {
            this.values.remove(0);
        }
        return this;
    }

    @Override
    public ListObjectUnit<T> merge(ListObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        this.values.addAll(that.values);
        this.limit = Math.max(this.limit, that.limit);
        int i = this.values.size();
        if (this.limit > 0 && i > this.limit) {
            List<T> list = this.values.subList(i - this.limit, i);
            this.values = new ArrayList<>(this.limit);
            this.values.addAll(list);
        }
        return this;
    }

    @Override
    public List<T> value() {
        return this.values;
    }

    @Override
    public ListObjectUnit<T> fastClone() {
        ListObjectUnit<T> mergeableListObject = new ListObjectUnit<>();
        mergeableListObject.limit = this.limit;
        for (T item : values) {
            mergeableListObject.values.add(item.fastClone());
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
        ListObjectUnit<T> thatUnit = (ListObjectUnit) that;
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
