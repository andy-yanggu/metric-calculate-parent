package com.yanggu.metric_calculate.core.aggregate_function.numeric;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.acc.KurtosisAccumulator;

/**
 * 峰度系数
 */
@Numerical
@AggregateFunctionAnnotation(name = "KURTOSIS", displayName = "峰度系数")
public class KurtosisAggregateFunction<T extends Number> implements AggregateFunction<T, KurtosisAccumulator, Double> {

    @Override
    public KurtosisAccumulator createAccumulator() {
        return new KurtosisAccumulator();
    }

    @Override
    public KurtosisAccumulator add(T value, KurtosisAccumulator accumulator) {
        accumulator.addValue(value.doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(KurtosisAccumulator accumulator) {
        return accumulator.calculateKurtosis();
    }

    @Override
    public KurtosisAccumulator merge(KurtosisAccumulator a, KurtosisAccumulator b) {
        a.merge(b);
        return a;
    }

}