package com.yanggu.metric_calculate.core.unit.count_window;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.fieldprocess.BaseAggregateFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
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
@MergeType(value = "LISTOBJECTCOUNTWINDOW", useParam = true, countWindow = true)
@Collective(useCompareField = false, retainObject = true)
public class ListObjectCountWindowUnit<T extends Cloneable2<T>> implements
        CollectionUnit<T, ListObjectCountWindowUnit<T>>, Value<Object>, Serializable, Iterable<T> {

    private static final long serialVersionUID = -1500607404480893613L;

    private List<T> values = new ArrayList<>();

    private Integer limit = 0;

    private BaseAggregateFieldProcessor<?> aggregateFieldProcessor;

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

        Object tempAggProcessor = param.get(Fields.aggregateFieldProcessor);
        if (tempAggProcessor instanceof BaseAggregateFieldProcessor) {
            this.aggregateFieldProcessor = ((BaseAggregateFieldProcessor<?>) tempAggProcessor);
        } else {
            throw new RuntimeException("需要设置聚合字段处理器");
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
        Value first = ((Value) CollUtil.getFirst(tempValueList));
        //TODO 这里不能直接使用unitFactory的initInstanceByValue方法
        //TODO 因为initValue是由聚合字段处理器生成的, 而不是原始的T
        //TODO 这里有问题, 传入的可能是标量值(基本数据类型), 不能都当做聚合值, 例如java对象
        MergedUnit mergedUnit = (MergedUnit) aggregateFieldProcessor.process(JSONUtil.parseObj(first.value()));
        for (int i = 1; i < tempValueList.size(); i++) {
            T t = tempValueList.get(i);
            mergedUnit.merge((MergedUnit) aggregateFieldProcessor.process(JSONUtil.parseObj(((Value) t).value())));
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
        mergeableListObject.aggregateFieldProcessor = this.aggregateFieldProcessor;
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
