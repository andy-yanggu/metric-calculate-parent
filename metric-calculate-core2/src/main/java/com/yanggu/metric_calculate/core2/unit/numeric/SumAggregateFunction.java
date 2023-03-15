package com.yanggu.metric_calculate.core2.unit.numeric;


import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

public class SumAggregateFunction<T extends Number> implements AggregateFunction<T, Double, Double> {

    @Override
    public Double createAccumulator() {
        return 0.0D;
    }

    @Override
    public Double add(T value, Double accumulator) {
        return value.doubleValue() + accumulator;
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double a, Double b) {
        return a + b;
    }

}
