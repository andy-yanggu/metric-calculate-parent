package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最小值
 *
 * @param <T>
 */
@Objective(keyStrategy = 3, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "MINVALUE", displayName = "最小值")
public class MinValueAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
