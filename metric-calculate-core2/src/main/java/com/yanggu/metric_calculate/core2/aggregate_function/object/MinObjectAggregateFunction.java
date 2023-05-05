package com.yanggu.metric_calculate.core2.aggregate_function.object;

import cn.hutool.core.lang.mutable.MutableObj;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最小对象
 *
 * @param <T>
 */
@MergeType("MINOBJECT")
@Objective(useCompareField = true, retainObject = true)
public class MinObjectAggregateFunction<T extends Comparable<T>> implements AggregateFunction<T, MutableObj<T>, T> {

    @Override
    public MutableObj<T> createAccumulator() {
        return new MutableObj<>();
    }

    @Override
    public MutableObj<T> add(T input, MutableObj<T> accumulator) {
        T oldValue = accumulator.get();
        //如果old为空
        if (oldValue == null || input.compareTo(oldValue) < 0) {
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
        } else if (thisValue.compareTo(thatValue) < 0) {
            return thisAccumulator;
        } else {
            return thatAccumulator;
        }
    }

}
