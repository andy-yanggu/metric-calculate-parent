package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 首次聚合函数抽象类
 * <p>最先写入的非NULL数据</p>
 *
 * @param <IN>
 */
public class AbstractFirstAggregateFunction<IN> implements AggregateFunction<IN, MutableObj<IN>, IN> {

    @Override
    public MutableObj<IN> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<IN> add(IN input, MutableObj<IN> accumulator) {
        IN oldValue = accumulator.get();
        if (oldValue == null && input != null) {
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
        if (thisAccumulator.get() == null && thatAccumulator.get() != null) {
            return thatAccumulator;
        } else {
            return thisAccumulator;
        }
    }

}
