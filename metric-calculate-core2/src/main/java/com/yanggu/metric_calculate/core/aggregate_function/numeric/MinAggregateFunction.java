package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;

/**
 * 最小值
 *
 * @param <T>
 */
@Numerical
@MergeType("MIN")
public class MinAggregateFunction<T extends Number> implements AggregateFunction<T, Double, Double> {

    @Override
    public Double createAccumulator() {
        return Double.MAX_VALUE;
    }

    @Override
    public Double add(T input, Double accumulator) {
        return Math.min(input.doubleValue(), accumulator);
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double thisAccumulator, Double thatAccumulator) {
        return Math.min(thisAccumulator, thatAccumulator);
    }

}
