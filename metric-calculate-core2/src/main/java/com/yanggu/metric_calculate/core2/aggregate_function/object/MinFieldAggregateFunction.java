package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最小字段
 *
 * @param <T>
 */
@MergeType("MINFIELD")
@Objective(useCompareField = true, retainObject = false)
public class MinFieldAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
