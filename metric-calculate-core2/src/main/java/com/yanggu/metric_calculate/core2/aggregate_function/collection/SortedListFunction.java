package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;

import java.util.List;


public class SortedListFunction<T extends Comparable<T>> implements AggregateFunction<T, BoundedPriorityQueue<T>, List<T>> {

    private Integer limit;

    @Override
    public BoundedPriorityQueue<T> createAccumulator() {
        return new BoundedPriorityQueue<>(limit);
    }

    @Override
    public BoundedPriorityQueue<T> add(T value, BoundedPriorityQueue<T> accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public List<T> getResult(BoundedPriorityQueue<T> accumulator) {
        return accumulator.toList();
    }

    @Override
    public BoundedPriorityQueue<T> merge(BoundedPriorityQueue<T> thisAccumulator, BoundedPriorityQueue<T> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        return thisAccumulator;
    }

}
