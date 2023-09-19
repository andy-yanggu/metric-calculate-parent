package com.yanggu.metric_calculate.core.aggregate_function.numeric;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import org.dromara.hutool.core.lang.mutable.MutableEntry;

/**
 * 平均值
 */
@Numerical
@AggregateFunctionAnnotation(name = "AVG", displayName = "平均值")
public class AvgAggregateFunction<T extends Number> implements AggregateFunction<T, MutableEntry<Double, Long>, Double> {

    @Override
    public MutableEntry<Double, Long> createAccumulator() {
        return new MutableEntry<>(0.0D, 0L);
    }

    @Override
    public MutableEntry<Double, Long> add(T input, MutableEntry<Double, Long> accumulator) {
        accumulator.setKey(accumulator.getKey() + input.doubleValue());
        accumulator.setValue(accumulator.getValue() + 1);
        return accumulator;
    }

    @Override
    public Double getResult(MutableEntry<Double, Long> accumulator) {
        return accumulator.getKey() / accumulator.getValue();
    }

    @Override
    public MutableEntry<Double, Long> merge(MutableEntry<Double, Long> thisAccumulator, MutableEntry<Double, Long> thatAccumulator) {
        thisAccumulator.setKey(thisAccumulator.getKey() + thatAccumulator.getKey());
        thisAccumulator.setValue(thisAccumulator.getValue() + thatAccumulator.getValue());
        return thisAccumulator;
    }

}
