package com.yanggu.metric_calculate.core2.unit;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2023/3/16 11:42
 * @description:
 */
public class Sum<T extends Number> implements AggregateFunction<T, T, T> {

    @Override
    public T createAccumulator() {
        return null;
    }

    @Override
    public T add(T value, T accumulator) {
        return null;
    }

    @Override
    public T getResult(T accumulator) {
        return null;
    }

    @Override
    public T merge(T a, T b) {
        return null;
    }
}
