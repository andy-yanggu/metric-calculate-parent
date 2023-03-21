package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

@MergeType("MAXFIELD")
@Objective(useCompareField = true, retainObject = false)
public class MaxFieldFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
