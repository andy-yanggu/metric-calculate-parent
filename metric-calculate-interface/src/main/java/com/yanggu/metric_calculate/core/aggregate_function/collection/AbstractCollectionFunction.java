package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

import java.util.Collection;

/**
 * 集合型抽象类
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

}
