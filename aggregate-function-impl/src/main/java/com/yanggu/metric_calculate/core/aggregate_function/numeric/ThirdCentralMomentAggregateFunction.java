package com.yanggu.metric_calculate.core.aggregate_function.numeric;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.agg_bean.ThirdCentralMomentAccumulator;

/**
 * 三阶中心炬
 */
@Numerical
@AggregateFunctionAnnotation(name = "MOM3", displayName = "三阶中心炬")
public class ThirdCentralMomentAggregateFunction<T extends Number> implements AggregateFunction<T, ThirdCentralMomentAccumulator, Double> {

    @Override
    public ThirdCentralMomentAccumulator createAccumulator() {
        return new ThirdCentralMomentAccumulator();
    }

    @Override
    public ThirdCentralMomentAccumulator add(T value, ThirdCentralMomentAccumulator accumulator) {
        accumulator.addValue(value.doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(ThirdCentralMomentAccumulator accumulator) {
        return accumulator.calculateThirdCentralMoment();
    }

    @Override
    public ThirdCentralMomentAccumulator merge(ThirdCentralMomentAccumulator a, ThirdCentralMomentAccumulator b) {
        a.merge(b);
        return a;
    }

}