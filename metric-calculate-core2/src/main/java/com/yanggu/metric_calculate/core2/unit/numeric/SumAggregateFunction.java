package com.yanggu.metric_calculate.core2.unit.numeric;


import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.math.BigDecimal;

public class SumAggregateFunction implements AggregateFunction<BigDecimal, BigDecimal, BigDecimal> {
    @Override
    public BigDecimal createAccumulator() {
        return new BigDecimal("0.0");
    }

    @Override
    public BigDecimal add(BigDecimal value, BigDecimal accumulator) {
        return accumulator.add(value);
    }

    @Override
    public BigDecimal getResult(BigDecimal accumulator) {
        return accumulator;
    }

    @Override
    public BigDecimal merge(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

}
