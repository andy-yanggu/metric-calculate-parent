package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.util.List;
import java.util.Set;

/**
 * 去重字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 1, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "DISTINCTLISTFIELD", displayName = "去重字段列表")
public class DistinctListFieldAggregateFunction<T> extends AbstractDistinctAggregateFunction<KeyValue<MultiFieldData, T>, List<T>> {

    @Override
    public List<T> getResult(Set<KeyValue<MultiFieldData, T>> acc) {
        return acc.stream()
                .map(KeyValue::getValue)
                .toList();
    }

}
