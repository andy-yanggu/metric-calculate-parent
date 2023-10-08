package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.agg_bean.VarianceAccumulator;

/**
 * 方差
 */
@Numerical
@AggregateFunctionAnnotation(name = "VARS", displayName = "方差")
public class VarianceAggregateFunction<T extends Number> implements AggregateFunction<T, VarianceAccumulator, Double> {

    @Override
    public VarianceAccumulator createAccumulator() {
        return new VarianceAccumulator();
    }

    @Override
    public VarianceAccumulator add(T value, VarianceAccumulator accumulator) {
        accumulator.addValue(value.doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(VarianceAccumulator accumulator) {
        return accumulator.calculateVariance();
    }

    @Override
    public VarianceAccumulator merge(VarianceAccumulator a, VarianceAccumulator b) {
        a.merge(b);
        return a;
    }

}