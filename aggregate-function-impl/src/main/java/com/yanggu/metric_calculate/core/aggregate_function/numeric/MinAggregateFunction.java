package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;

import java.math.BigDecimal;

/**
 * 最小数值
 *
 * @param <T>
 */
@Numerical
@AggregateFunctionAnnotation(name = "MIN", displayName = "最小数值")
public class MinAggregateFunction<T extends Number> implements AggregateFunction<T, BigDecimal, BigDecimal> {

    @Override
    public BigDecimal createAccumulator() {
        return BigDecimal.valueOf(Double.MAX_VALUE);
    }

    @Override
    public BigDecimal add(T input, BigDecimal accumulator) {
        if (input == null) {
            return accumulator;
        }
        BigDecimal inputValue = new BigDecimal(input.toString());
        return inputValue.min(accumulator);
    }

    @Override
    public BigDecimal getResult(BigDecimal accumulator) {
        return accumulator;
    }

    @Override
    public BigDecimal merge(BigDecimal thisAccumulator, BigDecimal thatAccumulator) {
        return thisAccumulator.min(thatAccumulator);
    }

}
