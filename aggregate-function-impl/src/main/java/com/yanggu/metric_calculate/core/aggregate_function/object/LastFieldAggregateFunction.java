package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最后写入的非NULL值
 *
 * @param <T>
 */
@Objective(keyStrategy = 0, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "LASTFIELD", displayName = "最后写入的非NULL值")
public class LastFieldAggregateFunction<T> extends LastObjectAggregateFunction<T> {
}
