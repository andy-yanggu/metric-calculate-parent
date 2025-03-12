package com.yanggu.metric_calculate.core.aggregate_function.numeric;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.dromara.hutool.core.math.NumberUtil;

import java.math.BigDecimal;

/**
 * 求和
 *
 * @param <T>
 */
@Numerical
@AggregateFunctionAnnotation(name = "SUM", displayName = "求和")
public class SumAggregateFunction<T extends Number> implements AggregateFunction<T, BigDecimal, BigDecimal> {

    @Override
    public BigDecimal createAccumulator() {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal add(T input, BigDecimal accumulator) {
        return NumberUtil.add(input, accumulator);
    }

    @Override
    public BigDecimal getResult(BigDecimal accumulator) {
        return accumulator;
    }

    @Override
    public BigDecimal merge(BigDecimal thisAccumulator, BigDecimal thatAccumulator) {
        return NumberUtil.add(thisAccumulator, thatAccumulator);
    }

}
