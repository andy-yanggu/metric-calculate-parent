package com.yanggu.metric_calculate.core.unit.count_window;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.core.enums.TimeWindowEnum.TIME_SLIDING_WINDOW;

/**
 * 滑动计数窗口, 窗口大小为limit, 滑动步长为1
 *
 * @param <T>
 */
@Data
@FieldNameConstants
@Collective(useCompareField = false, retainObject = true)
@MergeType(value = "LISTOBJECTCOUNTWINDOW", useParam = true, useSubAgg = true, timeWindowType = TIME_SLIDING_WINDOW)
public class ListObjectCountWindowUnit<T extends Cloneable2<T>> implements
        CollectionUnit<T, ListObjectCountWindowUnit<T>>, Value<Object>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -1500607404480893613L;

    private List<T> values = new ArrayList<>();

    private Integer limit = 0;

    public ListObjectCountWindowUnit() {
    }

    public ListObjectCountWindowUnit(Map<String, Object> param) {
        if (CollUtil.isEmpty(param)) {
            throw new RuntimeException("滑动计数窗口需要设置相关参数");
        }
        Object tempLimit = param.get(Fields.limit);
        if (tempLimit instanceof Integer) {
            this.limit = (Integer) tempLimit;
        } else {
            throw new RuntimeException("需要设置计数窗口大小");
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

    /**
     * add.
     *
     * @return
     */
    @Override
    public ListObjectCountWindowUnit<T> add(T value) {
        this.values.add(value);
        if (this.values.size() > this.limit) {
            this.values.remove(0);
        }
        return this;
    }

    @Override
    public ListObjectCountWindowUnit<T> merge(ListObjectCountWindowUnit<T> that) {
        this.values.addAll(that.values);
        int size = this.values.size();
        if (size > limit) {
            //将前面的数据移除掉
            this.values = CollUtil.sub(this.values, size - limit, size);
        }
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return this.values.iterator();
    }

    @SneakyThrows
    @Override
    public Object value() {
        return this.values.stream()
                .map(tempValue -> ((Value<?>) tempValue).value())
                .collect(Collectors.toList());
    }

    @Override
    public ListObjectCountWindowUnit<T> fastClone() {
        ListObjectCountWindowUnit<T> mergeableListObject = new ListObjectCountWindowUnit<>();
        mergeableListObject.limit = this.limit;
        for (T item : values) {
            mergeableListObject.getValues().add(item.fastClone());
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
        return Objects.equals(this.limit, thatUnit.limit);
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, list=%s}", this.limit, this.values.toString());
    }

}
