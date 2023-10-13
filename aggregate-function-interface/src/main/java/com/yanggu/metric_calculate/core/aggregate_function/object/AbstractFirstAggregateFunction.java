package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 首次聚合函数抽象类
 * <p>最先写入的非NULL数据</p>
 *
 * @param <T>
 */
public class AbstractFirstAggregateFunction<T> implements AggregateFunction<T, MutableObj<T>, T> {

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
