package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

public class TestSumAggregateFunction<T extends Number> implements AggregateFunction<T, Double, Double> {

    @Override
    public Double createAccumulator() {
        return 0.0D;
    }

    @Override
    public Double add(T input, Double accumulator) {
        return input.doubleValue() + accumulator;
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double thisAccumulator, Double thatAccumulator) {
        return thisAccumulator + thatAccumulator;
    }

}