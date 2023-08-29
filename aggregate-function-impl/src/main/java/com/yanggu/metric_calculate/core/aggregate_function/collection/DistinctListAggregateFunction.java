package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;

/**
 * 去重列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 1, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "DISTINCTLIST", displayName = "去重列表")
public class DistinctListAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
