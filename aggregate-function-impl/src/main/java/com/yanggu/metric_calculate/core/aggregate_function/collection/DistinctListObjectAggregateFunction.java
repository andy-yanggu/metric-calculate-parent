package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * 去重对象列表
 */
@Collective(keyStrategy = 1, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "DISTINCTLISTOBJECT", displayName = "去重对象列表")
public class DistinctListObjectAggregateFunction extends AbstractDistinctAggregateFunction<KeyValue<MultiFieldData, JSONObject>, List<JSONObject>> {

    @Override
    public List<JSONObject> getResult(Set<KeyValue<MultiFieldData, JSONObject>> acc) {
        return acc.stream()
                .map(KeyValue::getValue)
                .toList();
    }

}
