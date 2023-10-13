package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 最大值聚合函数抽象类
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <T> 输入数据类型
 * @param <OUT> 输出数据类型
 */
public abstract class AbstractMaxAggregateFunction<T extends Comparable<T>, OUT> implements AggregateFunction<T, MutableObj<T>, OUT> {

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