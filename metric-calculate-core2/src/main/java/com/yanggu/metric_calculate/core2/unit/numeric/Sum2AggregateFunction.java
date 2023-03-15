package com.yanggu.metric_calculate.core2.unit.numeric;


import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

public class Sum2AggregateFunction<T extends Number> implements AggregateFunction<T, T, T> {

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
        return null;
    }

    @Override
    public T merge(T a, T b) {
        return null;
    }

}
