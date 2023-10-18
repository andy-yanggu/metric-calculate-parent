package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.util.Set;

/**
 * 去重计数
 */
@Collective(keyStrategy = 1, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "DISTINCTCOUNT", displayName = "去重计数")
public class DistinctCountAggregateFunction extends AbstractDistinctAggregateFunction<MultiFieldData, Integer> {

    @Override
    public Integer getResult(Set<MultiFieldData> acc) {
        return acc.size();
    }

}
