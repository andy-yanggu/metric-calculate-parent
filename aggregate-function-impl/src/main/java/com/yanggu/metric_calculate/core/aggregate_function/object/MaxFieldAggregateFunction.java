package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import org.dromara.hutool.core.lang.mutable.MutableObj;

/**
 * 最大字段
 *
 * @param <T>
 */
@Objective(keyStrategy = 3, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "MAXFIELD", displayName = "最大字段")
public class MaxFieldAggregateFunction<T> extends AbstractMultiFieldDistinctKeyMaxAggregateFunction<T, T> {

    @Override
    public T getResult(MutableObj<KeyValue<MultiFieldDistinctKey, T>> accumulator) {
        return accumulator.get().getValue();
    }

}
