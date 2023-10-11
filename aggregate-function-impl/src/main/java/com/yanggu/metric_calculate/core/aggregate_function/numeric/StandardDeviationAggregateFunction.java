package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.acc.StandardDeviationAccumulator;

/**
 * 标准差
 */
@Numerical
@AggregateFunctionAnnotation(name = "VARP", displayName = "标准差")
public class StandardDeviationAggregateFunction<T extends Number> implements AggregateFunction<T, StandardDeviationAccumulator, Double> {

    @Override
    public StandardDeviationAccumulator createAccumulator() {
        return new StandardDeviationAccumulator();
    }

    @Override
    public StandardDeviationAccumulator add(T value, StandardDeviationAccumulator accumulator) {
        accumulator.addValue(value.doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(StandardDeviationAccumulator accumulator) {
        return accumulator.calculateStandardDeviation();
    }

    @Override
    public StandardDeviationAccumulator merge(StandardDeviationAccumulator a, StandardDeviationAccumulator b) {
        a.merge(b);
        return a;
    }

}