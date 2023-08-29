package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 当前行的第前N个字段
 *
 * @param <IN>
 */
@Objective(keyStrategy = 0, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "LAGFIELD", displayName = "当前行的第前N个字段")
public class LagFieldAggregateFunction<IN> extends LagObjectAggregateFunction<IN> {
}
