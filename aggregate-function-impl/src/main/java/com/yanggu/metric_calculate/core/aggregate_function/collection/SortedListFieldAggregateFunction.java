package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;

/**
 * 有序字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 2, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLISTFIELD", displayName = "有序字段列表")
public class SortedListFieldAggregateFunction<T> extends AbstractSortedListAggregateFunction<KeyValue<MultiFieldOrderCompareKey, T>, T> {

    @Override
    public T inToOut(KeyValue<MultiFieldOrderCompareKey, T> multiFieldOrderCompareKeyTKeyValue) {
        return multiFieldOrderCompareKeyTKeyValue.getValue();
    }

}
