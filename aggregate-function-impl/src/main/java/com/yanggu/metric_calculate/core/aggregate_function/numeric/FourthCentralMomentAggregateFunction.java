package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.agg_bean.FourthCentralMomentAccumulator;

/**
 * 四阶中心炬
 */
@Numerical
@AggregateFunctionAnnotation(name = "MOM4", displayName = "四阶中心炬")
public class FourthCentralMomentAggregateFunction<T extends Number> implements AggregateFunction<T, FourthCentralMomentAccumulator, Double> {

    @Override
    public FourthCentralMomentAccumulator createAccumulator() {
        return new FourthCentralMomentAccumulator();
    }

    @Override
    public FourthCentralMomentAccumulator add(T value, FourthCentralMomentAccumulator accumulator) {
        accumulator.addValue(value.doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(FourthCentralMomentAccumulator accumulator) {
        return accumulator.calculateFourthCentralMoment();
    }

    @Override
    public FourthCentralMomentAccumulator merge(FourthCentralMomentAccumulator a, FourthCentralMomentAccumulator b) {
        a.merge(b);
        return a;
    }

}