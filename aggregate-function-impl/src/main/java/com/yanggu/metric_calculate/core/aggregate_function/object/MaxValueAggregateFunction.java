package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最大值
 *
 * @param <T>
 */
@Objective(keyStrategy = 3, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "MAXVALUE", displayName = "最大值")
public class MaxValueAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
