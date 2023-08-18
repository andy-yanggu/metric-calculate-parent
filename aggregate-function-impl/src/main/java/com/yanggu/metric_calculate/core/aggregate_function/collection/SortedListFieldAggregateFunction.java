package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

/**
 * 有序字段列表
 *
 * @param <T>
 */
@MergeType("SORTEDLIMITLISTFIELD")
@Collective(keyStrategy = 2, retainStrategy = 1)
public class SortedListFieldAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
