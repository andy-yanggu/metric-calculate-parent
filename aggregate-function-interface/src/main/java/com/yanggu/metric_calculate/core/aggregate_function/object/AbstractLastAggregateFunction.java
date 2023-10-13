package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 最后写入的非NULL对象
 *
 * @param <T>
 */
public abstract class AbstractLastAggregateFunction<T> implements AggregateFunction<T, MutableObj<T>, T> {

    @Override
    public MutableObj<T> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<T> add(T input, MutableObj<T> accumulator) {
        accumulator.set(input);
        return accumulator;
    }

    @Override
    public T getResult(MutableObj<T> accumulator) {
        return accumulator.get();
    }

    @Override
    public MutableObj<T> merge(MutableObj<T> thisAccumulator, MutableObj<T> thatAccumulator) {
        if (thatAccumulator.get() != null) {
            return thatAccumulator;
        } else {
            return thisAccumulator;
        }
    }

}
