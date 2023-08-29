package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 最先写入的非NULL对象
 *
 * @param <T>
 */
@Objective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "FIRSTOBJECT", displayName = "最先写入的非NULL对象")
public class FirstObjectAggregateFunction<T> implements AggregateFunction<T, MutableObj<T>, T> {

    @Override
    public MutableObj<T> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<T> add(T input, MutableObj<T> accumulator) {
        T oldValue = accumulator.get();
        if (oldValue == null && input != null) {
            accumulator.set(input);
        }
        return accumulator;
    }

    @Override
    public T getResult(MutableObj<T> accumulator) {
        return accumulator.get();
    }

    @Override
    public MutableObj<T> merge(MutableObj<T> thisAccumulator, MutableObj<T> thatAccumulator) {
        if (thisAccumulator.get() == null && thatAccumulator.get() != null) {
            return thatAccumulator;
        } else {
            return thisAccumulator;
        }
    }

}
