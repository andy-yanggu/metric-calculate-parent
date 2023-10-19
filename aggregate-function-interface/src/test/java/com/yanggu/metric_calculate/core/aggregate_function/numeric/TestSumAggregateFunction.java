package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

public class TestSumAggregateFunction<IN extends Number> implements AggregateFunction<IN, Double, Double> {

    @Override
    public Double createAccumulator() {
        return 0.0D;
    }

    @Override
    public Double add(IN input, Double accumulator) {
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