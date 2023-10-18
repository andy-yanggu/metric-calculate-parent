package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;

/**
 * 最小值字段
 *
 * @param <T>
 */
@Objective(keyStrategy = 3, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "MINFIELD", displayName = "最小值字段")
public class MinFieldAggregateFunction<T> extends AbstractMultiFieldDataMinAggregateFunction<T, T> {

    @Override
    public T getResult(MutableObj<Pair<MultiFieldData, T>> accumulator) {
        return accumulator.get().getRight();
    }

}
