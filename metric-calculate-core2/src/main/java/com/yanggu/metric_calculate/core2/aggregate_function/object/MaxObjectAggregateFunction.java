package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;


public class MaxObjectAggregateFunction<T extends Comparable<T>> implements AggregateFunction<T, T, T> {

    @Override
    public T createAccumulator() {
        return null;
    }

    @Override
    public T add(T value, T accumulator) {
        return null;
    }

    @Override
    public T getResult(T accumulator) {
        return accumulator;
    }

    @Override
    public T merge(T thisAccumulator, T thatAccumulator) {
        return null;
    }

}
