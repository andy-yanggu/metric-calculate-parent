package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;

import java.util.List;

/**
 * 协方差
 */
@Numerical(multiNumber = true)
@AggregateFunctionAnnotation(name = "COV", displayName = "协方差")
public class CovAggregateFunction implements AggregateFunction<List<? extends Number>, Object, Double> {

    @Override
    public Object createAccumulator() {
        return null;
    }

    @Override
    public Object add(List<? extends Number> input, Object accumulator) {
        return null;
    }

    @Override
    public Double getResult(Object accumulator) {
        return null;
    }

    @Override
    public Object merge(Object thisAccumulator, Object thatAccumulator) {
        return null;
    }

}
