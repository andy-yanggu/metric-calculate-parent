package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.util.List;
import java.util.Set;

/**
 * 去重列表
 */
@Collective(keyStrategy = 1, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "DISTINCTLIST", displayName = "去重列表")
public class DistinctListAggregateFunction extends AbstractDistinctAggregateFunction<MultiFieldData, List<List<Object>>> {

    @Override
    public List<List<Object>> getResult(Set<MultiFieldData> acc) {
        return acc.stream()
                .map(MultiFieldData::getFieldList)
                .toList();
    }

}
