package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

@MergeType("SORTEDLIMITLISTFIELD")
@Collective(useSortedField = true, retainStrategy = 1)
public class SortedListFieldAggregateFunction<T extends Comparable<T>> extends SortedListObjectAggregateFunction<T> {
}
