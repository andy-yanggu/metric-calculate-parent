package com.yanggu.metric_calculate.core.unit.count_window;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 滑动计数窗口, 窗口大小为limit, 滑动步长为1
 *
 * @param <T>
 */
@Data
@FieldNameConstants
@MergeType(value = "LISTOBJECTCOUNTWINDOW", useParam = true)
@Collective(useCompareField = false, retainObject = true)
public class ListObjectCountWindowUnit<T extends Cloneable2<T>> implements
        CollectionUnit<T, ListObjectCountWindowUnit<T>>, Value<Object>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -1500607404480893613L;

    private List<T> values = new ArrayList<>();

    private Integer limit = 0;

    private UnitFactory unitFactory;

    private String aggregateType;

    private Map<String, Object> countWindowAggParams;

    public ListObjectCountWindowUnit() {
    }

    public ListObjectCountWindowUnit(Map<String, Object> param) {
        if (CollUtil.isEmpty(param)) {
            return;
        }
        Object tempLimit = param.get(Fields.limit);
        if (tempLimit instanceof Integer) {
            this.limit = (Integer) tempLimit;
        } else {
            throw new RuntimeException("需要设置计数窗口大小");
        }

        Object tempAggregateType = param.get(Fields.aggregateType);
        if (StrUtil.isBlankIfStr(tempAggregateType)) {
            throw new RuntimeException("聚合类型为空");
        } else {
            aggregateType = tempAggregateType.toString();
        }

        Object tempUnitFactory = param.get(Fields.unitFactory);
        if (tempUnitFactory instanceof UnitFactory) {
            unitFactory = (UnitFactory) tempUnitFactory;
            Object tempCountWindowAggParam = param.get(Fields.countWindowAggParams);
            if (tempCountWindowAggParam instanceof Map) {
                countWindowAggParams = (Map<String, Object>) tempCountWindowAggParam;
            }
        } else {
            throw new RuntimeException("UnitFactory为空");
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
        if (this.limit > 0 && this.values.size() > this.limit) {
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
        List<T> tempValueList = this.values;
        T first = CollUtil.getFirst(tempValueList);
        MergedUnit mergedUnit = unitFactory.initInstanceByValue(aggregateType, first, countWindowAggParams);
        for (int i = 1; i < tempValueList.size(); i++) {
            T t = tempValueList.get(i);
            mergedUnit.merge(unitFactory.initInstanceByValue(aggregateType, t, countWindowAggParams));
        }
        return ValueMapper.value((Value<?>) mergedUnit);
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
        return this.limit == thatUnit.limit;
    }

    @Override
    public String toString() {
        return String.format("{limit=%d, list=%s}", this.limit, this.values.toString());
    }

}
