package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

/**
 * 有序列表
 *
 * @param <T>
 */
@MergeType(value = "SORTEDLIMITLIST", displayName = "有序列表")
@Collective(keyStrategy = 2, retainStrategy = 0)
public class SortedListAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
