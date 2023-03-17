package com.yanggu.metric_calculate.core2.aggregate_function.numeric;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;

/**
 * 最大值
 *
 * @param <T>
 */
@Numerical
@MergeType("MAX")
public class MaxAggregateFunction<T extends Number> implements AggregateFunction<T, Double, Double> {

    @Override
    public Double createAccumulator() {
        return Double.MIN_VALUE;
    }

    @Override
    public Double add(T value, Double accumulator) {
        return Math.max(value.doubleValue(), accumulator);
    }

    @Override
    public Double getResult(Double accumulator) {
        return accumulator;
    }

    @Override
    public Double merge(Double thisAccumulator, Double thatAccumulator) {
        return Math.max(thisAccumulator, thatAccumulator);
    }

}
