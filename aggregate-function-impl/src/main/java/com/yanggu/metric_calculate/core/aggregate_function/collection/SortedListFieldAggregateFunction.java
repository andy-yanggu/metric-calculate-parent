package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

/**
 * 有序字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 2, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLISTFIELD", displayName = "有序字段列表")
public class SortedListFieldAggregateFunction<T> extends AbstractMultiFieldSortedListAggregateFunction<T, T> {

    @Override
    public T inToOut(Pair<MultiFieldData, T> pair) {
        return pair.getRight();
    }

}
