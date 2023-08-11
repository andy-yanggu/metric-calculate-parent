package com.yanggu.metric_calculate.core.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最大对象
 *
 * @param <T>
 */
@MergeType("MAXOBJECT")
@Objective(keyStrategy = 3, retainStrategy = 2)
public class MaxObjectAggregateFunction<T extends Comparable<T>> implements AggregateFunction<T, MutableObj<T>, T> {

    @Override
    public MutableObj<T> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<T> add(T input, MutableObj<T> accumulator) {
        T oldValue = accumulator.get();
        //如果old为空
        if (oldValue == null || input.compareTo(oldValue) > 0) {
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
        T thisValue = thisAccumulator.get();
        T thatValue = thatAccumulator.get();
        if (thisValue == null && thatValue != null) {
            return thatAccumulator;
        } else if (thisValue == null/* && thatValue == null*/) {
            return thisAccumulator;
        } else if (/*thisValue != null && */thatValue == null) {
            return thisAccumulator;
        } else if (thisValue.compareTo(thatValue) > 0) {
            return thisAccumulator;
        } else {
            return thatAccumulator;
        }
    }

}
