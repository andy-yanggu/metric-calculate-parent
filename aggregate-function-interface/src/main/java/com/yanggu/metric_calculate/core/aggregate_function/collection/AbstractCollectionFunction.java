package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

import java.util.Collection;

/**
 * 集合型抽象类
 * <p>主要是定义了ACC类型，为Collection的子类</p>
 * <p>定义了add方法，直接调用{@link Collection#add(Object)}方法</p>
 * <p>定义了merge方法，直接调用{@link Collection#addAll(Collection)}方法</p>
 * <p>子类需要重写{@link AggregateFunction#createAccumulator()}方法和{@link AggregateFunction#getResult(Object)}</p>
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public abstract class AbstractCollectionFunction<IN, ACC extends Collection<IN>, OUT>
        implements AggregateFunction<IN, ACC, OUT> {

    @Override
    public ACC add(IN input, ACC accumulator) {
        accumulator.add(input);
        return accumulator;
    }

    @Override
    public ACC merge(ACC thisAccumulator, ACC thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        return thisAccumulator;
    }

}
