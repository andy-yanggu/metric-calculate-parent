package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最先写入的非NULL字段
 *
 * @param <T>
 */
@Objective(keyStrategy = 0, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "FIRSTFIELD", displayName = "最先写入的非NULL字段")
public class FirstFieldAggregateFunction<T> extends AbstractFirstAggregateFunction<T> {
}
