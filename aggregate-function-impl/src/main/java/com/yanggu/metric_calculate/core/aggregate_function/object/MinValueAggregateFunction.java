package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最小值
 *
 * @param <T>
 */
@MergeType(value = "MINVALUE", displayName = "最小值")
@Objective(keyStrategy = 3, retainStrategy = 0)
public class MinValueAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
