package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

import java.util.Map;

/**
 * 当前行的第前N条对象
 */
@Objective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LAGOBJECT", displayName = "当前行的第前N条对象")
public class LagObjectAggregateFunction extends AbstractLagAggregateFunction<Map<String, Object>> {
}
