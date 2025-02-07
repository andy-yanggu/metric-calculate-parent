package com.yanggu.metric_calculate.core.aggregate_function.numeric;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.pojo.acc.CovarianceAccumulator;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.util.List;

/**
 * 协方差
 */
@Numerical(multiNumber = true)
@AggregateFunctionAnnotation(name = "COV", displayName = "协方差")
public class CovarianceAggregateFunction implements AggregateFunction<MultiFieldData, CovarianceAccumulator, Double> {

    @Override
    public CovarianceAccumulator createAccumulator() {
        return new CovarianceAccumulator();
    }

    @Override
    public CovarianceAccumulator add(MultiFieldData multiFieldData, CovarianceAccumulator accumulator) {
        List<? extends Number> list = multiFieldData.getFieldList().stream()
                .map(Number.class::cast)
                .toList();
        accumulator.addValue(list.get(0).doubleValue(), list.get(1).doubleValue());
        return accumulator;
    }

    @Override
    public Double getResult(CovarianceAccumulator accumulator) {
        return accumulator.calculateCovariance();
    }

    @Override
    public CovarianceAccumulator merge(CovarianceAccumulator a, CovarianceAccumulator b) {
        a.merge(b);
        return a;
    }

}