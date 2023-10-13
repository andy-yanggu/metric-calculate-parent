package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;

import java.util.List;

/**
 * 有序列表
 */
@Collective(keyStrategy = 2, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLIST", displayName = "有序列表")
public class SortedListAggregateFunction extends AbstractMultiFieldSortedListAggregateFunction<Void, List<Object>> {

    @Override
    public List<Object> inToOut(KeyValue<MultiFieldDistinctKey, Void> in) {
        return in.getKey().getFieldList();
    }

}
