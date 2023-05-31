package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

@MergeType("SORTEDLIMITLIST")
@Collective(useSortedField = true, retainStrategy = 0)
public class SortedListAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
