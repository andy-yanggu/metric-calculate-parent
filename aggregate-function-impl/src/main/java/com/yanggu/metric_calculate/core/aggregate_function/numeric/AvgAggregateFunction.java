package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.dromara.hutool.core.lang.mutable.MutablePair;

/**
 * 平均值
 */
@Numerical
@AggregateFunctionAnnotation(name = "AVG", displayName = "平均值")
public class AvgAggregateFunction<T extends Number> implements AggregateFunction<T, MutablePair<Double, Long>, Double> {

    @Override
    public MutablePair<Double, Long> createAccumulator() {
        return new MutablePair<>(0.0D, 0L);
    }

    @Override
    public MutablePair<Double, Long> add(T input, MutablePair<Double, Long> accumulator) {
        accumulator.setLeft(accumulator.getLeft() + input.doubleValue());
        accumulator.setRight(accumulator.getRight() + 1);
        return accumulator;
    }

    @Override
    public Double getResult(MutablePair<Double, Long> accumulator) {
        return accumulator.getLeft() / accumulator.getRight();
    }

    @Override
    public MutablePair<Double, Long> merge(MutablePair<Double, Long> thisAccumulator, MutablePair<Double, Long> thatAccumulator) {
        thisAccumulator.setLeft(thisAccumulator.getLeft() + thatAccumulator.getLeft());
        thisAccumulator.setRight(thisAccumulator.getRight() + thatAccumulator.getRight());
        return thisAccumulator;
    }

}
