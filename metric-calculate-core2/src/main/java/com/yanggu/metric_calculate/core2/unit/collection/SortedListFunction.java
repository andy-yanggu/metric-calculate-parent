package com.yanggu.metric_calculate.core2.unit.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.List;


public class SortedListFunction<T extends Comparable<T>> implements AggregateFunction<T, BoundedPriorityQueue<T>, List<T>> {

    @Override
    public BoundedPriorityQueue<T> createAccumulator() {
        return new BoundedPriorityQueue<>(10);
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
    public BoundedPriorityQueue<T> merge(BoundedPriorityQueue<T> a, BoundedPriorityQueue<T> b) {
        a.addAll(b);
        return a;
    }

}
