package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最大字段
 *
 * @param <T>
 */
@MergeType("MAXFIELD")
@Objective(useCompareField = true, retainObject = false)
public class MaxFieldAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
