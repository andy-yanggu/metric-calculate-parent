package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 取代对象
 *
 * @param <T>
 */
@MergeType("REPLACEDOBJECT")
@Objective(useCompareField = false, retainObject = true)
public class ReplacedObjectAggregateFunction<T> implements AggregateFunction<T, MutableObj<T>, T> {

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
        if (thisAccumulator.get() != null) {
            return thisAccumulator;
        } else {
            return thatAccumulator;
        }
    }

}
