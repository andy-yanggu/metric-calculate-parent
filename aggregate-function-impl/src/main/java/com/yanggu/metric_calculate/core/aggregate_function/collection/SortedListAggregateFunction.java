package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;

/**
 * 有序列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 2, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLIST", displayName = "有序列表")
public class SortedListAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
