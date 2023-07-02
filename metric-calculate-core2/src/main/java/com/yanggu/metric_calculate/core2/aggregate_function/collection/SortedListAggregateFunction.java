package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;

/**
 * 有序列表
 *
 * @param <T>
 */
@MergeType("SORTEDLIMITLIST")
@Collective(keyStrategy = 2, retainStrategy = 0)
public class SortedListAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
