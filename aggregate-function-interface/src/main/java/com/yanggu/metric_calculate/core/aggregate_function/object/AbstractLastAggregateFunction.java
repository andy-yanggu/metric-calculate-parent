package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 最后写入的非NULL对象
 *
 * @param <IN>
 */
public abstract class AbstractLastAggregateFunction<IN> implements AggregateFunction<IN, MutableObj<IN>, IN> {

    @Override
    public MutableObj<IN> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<IN> add(IN input, MutableObj<IN> accumulator) {
        if (input != null) {
            accumulator.set(input);
        }
        return accumulator;
    }

    @Override
    public IN getResult(MutableObj<IN> accumulator) {
        return accumulator.get();
    }

    @Override
    public MutableObj<IN> merge(MutableObj<IN> thisAccumulator, MutableObj<IN> thatAccumulator) {
        if (thatAccumulator.get() != null) {
            return thatAccumulator;
        } else {
            return thisAccumulator;
        }
    }

}
