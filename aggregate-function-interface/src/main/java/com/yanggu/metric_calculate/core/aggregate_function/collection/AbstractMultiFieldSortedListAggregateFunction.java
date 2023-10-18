package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.ListObjectComparator;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 多字段有序列表抽象类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractMultiFieldSortedListAggregateFunction<IN, OUT> extends AbstractSortedListAggregateFunction<Pair<MultiFieldData, IN>, OUT> {

    @AggregateFunctionFieldAnnotation(displayName = "升序和降序", notNull = true)
    private List<Boolean> booleanList;

    private ListObjectComparator<IN> comparator;

    @Override
    public void init() {
        ListObjectComparator<IN> tempComparator = new ListObjectComparator<>();
        tempComparator.setBooleanList(booleanList);
        this.comparator = tempComparator;
    }

    @Override
    public BoundedPriorityQueue<Pair<MultiFieldData, IN>> createAccumulator() {
        return new BoundedPriorityQueue<>(getLimit(), comparator);
    }

    @Override
    public abstract OUT inToOut(Pair<MultiFieldData, IN> pair);

}
