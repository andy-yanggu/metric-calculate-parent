package com.yanggu.metric_calculate.core.unit.collection;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.UnlimitedMergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.SneakyThrows;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.constant.Constant.COUNT_WINDOW_AGG_PARAM;
import static com.yanggu.metric_calculate.core.constant.Constant.UNIT_FACTORY;

@FieldNameConstants
@MergeType(value = "LISTOBJECTCOUNTWINDOW", useParam = true)
@Collective(useCompareField = false, retainObject = true)
public class ListObjectCountWindowUnit<T extends Cloneable2<T>> implements UnlimitedMergedUnit<ListObjectCountWindowUnit<T>>,
        CollectionUnit<T, ListObjectCountWindowUnit<T>>, Value<Object>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -1500607404480893613L;

    private List<T> values = new ArrayList<>();

    private Integer limit = 0;

    private UnitFactory unitFactory;

    private String aggregateType;

    private Map<String, Object> countWindowAggParams;

    public ListObjectCountWindowUnit() {
    }

    public ListObjectCountWindowUnit(Map<String, Object> param) throws Exception {
        if (CollUtil.isEmpty(param)) {
            return;
        }
        Object tempLimit = param.get(Fields.limit);
        if (tempLimit instanceof Integer) {
            this.limit = (Integer) tempLimit;
        } else {
            throw new RuntimeException("需要设置计数窗口大小");
        }

        Object aggregateType = param.get("aggregateType");
        if (StrUtil.isBlankIfStr(aggregateType)) {
            throw new RuntimeException("聚合类型为空");
        }

        Object tempUnitFactory = param.get(UNIT_FACTORY);
        if (tempUnitFactory instanceof UnitFactory) {
            Class<? extends MergedUnit<?>> mergeableClass = ((UnitFactory) tempUnitFactory)
                    .getMergeableClass(aggregateType.toString());
            Object tempCountWindowAggParam = param.get(COUNT_WINDOW_AGG_PARAM);
            if (tempCountWindowAggParam instanceof Map) {
            }
        }

    }

    public ListObjectCountWindowUnit(T value) {
        this();
        add(value);
    }

    /**
     * Construct.
     */
    public ListObjectCountWindowUnit(T value, int limit) {
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
     *
     * @return
     */
    @Override
    public ListObjectCountWindowUnit<T> add(T value) {
        this.values.add(value);
        if (this.limit > 0 && this.values.size() > this.limit) {
            this.values.remove(0);
        }
        return this;
    }

    public List<T> asList() {
        return this.values;
    }

    @Override
    public ListObjectCountWindowUnit<T> merge(ListObjectCountWindowUnit<T> that) {
        return merge(that, false);
    }

    @Deprecated
    private ListObjectCountWindowUnit<T> merge(ListObjectCountWindowUnit<T> that, boolean useLimit) {
        if (that == null) {
            return this;
        }
        List<T> values = that.getList();
        int limit = that.limit;
        return internalMerge(values, useLimit, limit);
    }

    private ListObjectCountWindowUnit<T> internalMerge(List<T> values, boolean useLimit, int limit) {
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
    public ListObjectCountWindowUnit<T> unlimitedMerge(ListObjectCountWindowUnit<T> that) {
        return merge(that, true);
    }

    @Override
    public Iterator<T> iterator() {
        return this.values.iterator();
    }

    @SneakyThrows
    @Override
    public List<T> value() {
        List<T> tempValueList = this.values;

        T first = CollUtil.getFirst(tempValueList);
        MergedUnit mergedUnit = unitFactory.initInstanceByValue(aggregateType, first, countWindowAggParams);


        return tempValueList;
    }

    @Override
    public ListObjectCountWindowUnit<T> fastClone() {
        ListObjectCountWindowUnit<T> mergeableListObject = new ListObjectCountWindowUnit<>();
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
        ListObjectCountWindowUnit<T> thatUnit = (ListObjectCountWindowUnit) that;
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
