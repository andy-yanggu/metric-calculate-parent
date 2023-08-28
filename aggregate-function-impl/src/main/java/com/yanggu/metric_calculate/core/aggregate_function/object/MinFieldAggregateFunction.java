package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最小值字段
 *
 * @param <T>
 */
@MergeType(value = "MINFIELD", displayName = "最小值字段")
@Objective(keyStrategy = 3, retainStrategy = 1)
public class MinFieldAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
