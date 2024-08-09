package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.dromara.hutool.json.JSONObject;

/**
 * 对象列表
 */
@Collective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LISTOBJECT", displayName = "对象列表")
public class ListObjectAggregateFunction extends AbstractListAggregateFunction<JSONObject> {
}
