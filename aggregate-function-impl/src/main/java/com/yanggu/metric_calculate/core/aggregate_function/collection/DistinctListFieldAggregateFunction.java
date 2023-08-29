package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;

/**
 * 去重字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 1, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "DISTINCTLISTFIELD", displayName = "去重字段列表")
public class DistinctListFieldAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
