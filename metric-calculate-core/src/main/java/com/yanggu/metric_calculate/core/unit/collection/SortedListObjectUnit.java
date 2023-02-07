package com.yanggu.metric_calculate.core.unit.collection;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@FieldNameConstants
@MergeType(value = "SORTEDLISTOBJECT", useParam = true)
@Collective(useCompareField = true, retainObject = true)
public class SortedListObjectUnit<T extends Comparable<T> & Cloneable2<T>> implements CollectionUnit<T, SortedListObjectUnit<T>>, Value<List<Object>> {

    protected Boolean desc = true;

    /**
     * 是否只展示value, 不展示key
     */
    protected Boolean onlyShowValue = true;

    protected int limit = 0;

    private List<T> original = new ArrayList<>();

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
        Object tempDesc = params.get(Fields.desc);
        if (tempDesc instanceof Boolean) {
            this.desc = (boolean) tempDesc;
        }
    }

    public SortedListObjectUnit(T value) {
        this(value, 0, true);
    }

    /**
     * Constructor.
     *
     * @param value value
     * @param limit list limit
     * @param desc  des or not
     */
    public SortedListObjectUnit(T value, int limit, boolean desc) {
        this();
        this.limit = limit;
        this.desc = desc;
        add(value);
    }

    public SortedListObjectUnit(T value, boolean desc) {
        this(value, 0, desc);
    }

    public SortedListObjectUnit(T value, int limit) {
        this(value, limit, true);
    }

    public int limit() {
        return limit;
    }

    public List<T> original() {
        return original;
    }

    public List<T> getList() {
        return this.original;
    }

    public boolean desc() {
        return desc;
    }

    /**
     * add element.
     *
     * @param value value
     * @return
     */
    @Override
    public SortedListObjectUnit<T> add(T value) {
        if (this.original.isEmpty()) {
            this.original.add(value);
            return this;
        }
        int i = 0;
        int j = this.original.size();
        while (j - i > 1) {
            int k = (i + j) / 2;
            Comparable<T> comparable = this.original.get(k);
            int m = comparable.compareTo(value);
            if ((!this.desc && m > 0) || (this.desc && m < 0)) {
                j = k;
                continue;
            }
            if ((!this.desc && m < 0) || (this.desc && m > 0)) {
                i = k;
                continue;
            }
            i = k;
        }
        if ((this.desc && this.original.get(i).compareTo(value) <= 0) || (!this.desc && this.original.get(i).compareTo(value) >= 0)) {
            this.original.add(i, value);
        } else {
            this.original.add(i + 1, value);
        }
        if (this.limit > 0 && this.original.size() > this.limit) {
            this.original.remove(this.original.size() - 1);
        }
        return this;
    }

    @Override
    public SortedListObjectUnit<T> merge(SortedListObjectUnit<T> that) {
        if (that == null) {
            return this;
        }
        return internalMerge(that.desc(), that.limit(), that.original());
    }

    private SortedListObjectUnit<T> internalMerge(boolean desc, int limit, List<T> original) {
        this.desc = desc;
        this.limit = Math.max(this.limit, limit);
        ArrayList<T> arrayList = new ArrayList<>();
        byte b1 = 0;
        byte b2 = 0;
        int i = this.original.size();
        int j = original.size();
        while (b1 < i || b2 < j) {
            T c1 = null;
            T c2 = null;
            if (b1 < i) {
                c1 = this.original.get(b1);
            }
            if (b2 < j) {
                c2 = original.get(b2);
            }
            if (c2 != null && c1 != null) {
                if ((this.desc && c1.compareTo(c2) >= 0) || (!this.desc && c1.compareTo(c2) <= 0)) {
                    arrayList.add(c1);
                    b1++;
                    continue;
                }
                arrayList.add(c2);
                b2++;
                continue;
            }
            if (c2 != null) {
                arrayList.add(c2);
                b2++;
                continue;
            }
            if (c1 != null) {
                arrayList.add(c1);
                b1++;
            }
        }
        if (this.limit > 0 && arrayList.size() > this.limit) {
            ArrayList<T> arrayList1 = new ArrayList<>(this.limit);
            arrayList1.addAll(arrayList.subList(0, this.limit));
            this.original = arrayList1;
        } else {
            this.original = arrayList;
        }
        return this;
    }

    @Override
    public SortedListObjectUnit<T> fastClone() {
        SortedListObjectUnit<T> mergeableSortedList = new SortedListObjectUnit<>();
        mergeableSortedList.desc = this.desc;
        mergeableSortedList.limit = this.limit;
        for (T item : getList()) {
            mergeableSortedList.getList().add(item.fastClone());
        }
        return mergeableSortedList;
    }

    @Override
    public List<Object> value() {
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
        if (this.desc != thatUnit.desc) {
            return false;
        }
        if (this.limit != thatUnit.limit) {
            return false;
        }
        if (this.original == null) {
            return thatUnit.original == null;
        } else {
            return this.original.equals(thatUnit.original);
        }
    }

    @Override
    public String toString() {
        return String.format("%s{limit=%s, desc=%s, list=%s}", getClass().getSimpleName(), limit, desc, original);
    }

}
