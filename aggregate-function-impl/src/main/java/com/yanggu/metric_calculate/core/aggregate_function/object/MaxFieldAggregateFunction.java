package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最大字段
 *
 * @param <T>
 */
@Objective(keyStrategy = 3, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "MAXFIELD", displayName = "最大字段")
public class MaxFieldAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
