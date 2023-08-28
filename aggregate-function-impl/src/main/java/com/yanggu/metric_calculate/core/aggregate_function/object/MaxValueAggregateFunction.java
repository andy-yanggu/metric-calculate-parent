package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最大值
 *
 * @param <T>
 */
@MergeType(value = "MAXVALUE", displayName = "最大值")
@Objective(keyStrategy = 3, retainStrategy = 0)
public class MaxValueAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
