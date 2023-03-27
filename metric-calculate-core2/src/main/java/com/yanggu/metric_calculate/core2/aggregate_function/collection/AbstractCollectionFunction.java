package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;

import java.util.Collection;

public abstract class AbstractCollectionFunction<IN, ACC extends Collection<IN>, OUT>
        implements AggregateFunction<IN, ACC, OUT> {

    @Override
    public ACC add(IN value, ACC accumulator) {
        accumulator.add(value);
        return accumulator;
    }

}
