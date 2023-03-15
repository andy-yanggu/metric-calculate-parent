package com.yanggu.metric_calculate.core2.unit.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core2.KeyValue;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.List;
import java.util.stream.Collectors;


public class SortedListFunction<T> implements AggregateFunction<KeyValue<MultiFieldOrderCompareKey, T>,
        BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>>, List<T>> {


    @Override
    public BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> createAccumulator() {
        return new BoundedPriorityQueue<>(10);
    }

    @Override
    public BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> add(KeyValue<MultiFieldOrderCompareKey, T> value,
                                                                        BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public List<T> getResult(BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> accumulator) {
        return accumulator.toList().stream()
                .map(KeyValue::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> merge(BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> a, BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, T>> b) {
        a.addAll(b);
        return a;
    }

}
