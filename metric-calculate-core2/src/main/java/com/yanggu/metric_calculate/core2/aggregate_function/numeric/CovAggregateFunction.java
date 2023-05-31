package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;

import java.util.List;

/**
 * 协方差
 */
@MergeType("COV")
@Numerical(multiNumber = true)
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
