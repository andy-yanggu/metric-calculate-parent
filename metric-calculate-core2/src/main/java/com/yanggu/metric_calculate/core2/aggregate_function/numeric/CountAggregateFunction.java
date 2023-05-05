package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Numerical;

/**
 * 计数
 * <p>非空的情况下进行+1</p>
 */
@Numerical
@MergeType("COUNT")
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
