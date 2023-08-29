package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;

/**
 * 有序字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 2, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLISTFIELD", displayName = "有序字段列表")
public class SortedListFieldAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
