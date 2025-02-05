package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 去重对象列表
 */
@Collective(keyStrategy = 1, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "DISTINCTLISTOBJECT", displayName = "去重对象列表")
public class DistinctListObjectAggregateFunction extends AbstractDistinctAggregateFunction<Pair<MultiFieldData, Map<String, Object>>, List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> getResult(Set<Pair<MultiFieldData, Map<String, Object>>> acc) {
        return acc.stream()
                .map(Pair::getRight)
                .toList();
    }

}
