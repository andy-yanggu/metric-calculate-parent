package com.yanggu.metric_calculate.core2.aggregate_function.numeric;

import cn.hutool.core.lang.mutable.MutablePair;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Numerical;

/**
 * 平均值
 */
@Numerical
@MergeType("AVG")
public class AvgAggregateFunction<T extends Number> implements AggregateFunction<T, MutablePair<Double, Long>, Double> {
    
    @Override
    public MutablePair<Double, Long> createAccumulator() {
        return new MutablePair<>(0.0D, 0L);
    }

    @Override
    public MutablePair<Double, Long> add(T input, MutablePair<Double, Long> accumulator) {
        accumulator.setKey(accumulator.getKey() + input.doubleValue());
        accumulator.setValue(accumulator.getValue() + 1);
        return accumulator;
    }

    @Override
    public Double getResult(MutablePair<Double, Long> accumulator) {
        return accumulator.getKey() / accumulator.getValue();
    }

    @Override
    public MutablePair<Double, Long> merge(MutablePair<Double, Long> thisAccumulator, MutablePair<Double, Long> thatAccumulator) {
        thisAccumulator.setKey(thisAccumulator.getKey() + thatAccumulator.getKey());
        thisAccumulator.setValue(thisAccumulator.getValue() + thatAccumulator.getValue());
        return thisAccumulator;
    }

}
