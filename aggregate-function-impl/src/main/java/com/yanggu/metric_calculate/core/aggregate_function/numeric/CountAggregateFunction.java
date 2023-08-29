package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;

/**
 * 计数
 * <p>非空的情况下进行+1</p>
 */
@Numerical
@AggregateFunctionAnnotation(name = "COUNT", displayName = "计数")
public class CountAggregateFunction<T> implements AggregateFunction<T, Long, Long> {

    @Override
    public Long createAccumulator() {
        return 0L;
    }

    @Override
    public Long add(T input, Long accumulator) {
        if (input != null) {
            return accumulator + 1L;
        } else {
            return accumulator;
        }
    }

    @Override
    public Long getResult(Long accumulator) {
        return accumulator;
    }

    @Override
    public Long merge(Long thisAccumulator, Long thatAccumulator) {
        return thisAccumulator + thatAccumulator;
    }

}
