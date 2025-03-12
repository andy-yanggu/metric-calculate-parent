package com.yanggu.metric_calculate.core.aggregate_function.numeric;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;

import java.math.BigDecimal;

/**
 * 最大数值
 *
 * @param <T>
 */
@Numerical
@AggregateFunctionAnnotation(name = "MAX", displayName = "最大数值")
public class MaxAggregateFunction<T extends Number> implements AggregateFunction<T, BigDecimal, BigDecimal> {

    @Override
    public BigDecimal createAccumulator() {
        return BigDecimal.valueOf(Double.MIN_VALUE);
    }

    @Override
    public BigDecimal add(T input, BigDecimal accumulator) {
        if (input == null) {
            return accumulator;
        }
        BigDecimal inputValue = new BigDecimal(input.toString());
        if (accumulator == null) {
            return inputValue;
        }
        return accumulator.max(inputValue);
    }

    @Override
    public BigDecimal getResult(BigDecimal accumulator) {
        return accumulator;
    }

    @Override
    public BigDecimal merge(BigDecimal thisAccumulator, BigDecimal thatAccumulator) {
        if (thisAccumulator == null) {
            return thatAccumulator;
        }
        if (thatAccumulator == null) {
            return thisAccumulator;
        }
        return thisAccumulator.max(thatAccumulator);
    }

}
