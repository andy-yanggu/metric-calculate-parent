package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

@MergeType("SORTEDLISTFIELD")
@Collective(useSortedField = true, retainObject = false)
public class SortedListFieldAggregateAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
