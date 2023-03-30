package com.yanggu.metric_calculate.core2.aggregate_function.numeric;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;

/**
 * 求和
 *
 * @param <T>
 */
@Numerical
@MergeType("SUM")
public class SumAggregateFunction<T extends Number> implements AggregateFunction<T, Double, T> {

    @Override
    public Double createAccumulator() {
        return 0.0D;
    }

    @Override
    public Double add(T input, Double accumulator) {
        return input.doubleValue() + accumulator;
    }

    @Override
    public T getResult(Double accumulator) {
        return ((T) accumulator);
    }

    @Override
    public Double merge(Double thisAccumulator, Double thatAccumulator) {
        return thisAccumulator + thatAccumulator;
    }

}
