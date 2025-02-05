package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

import java.util.Map;

/**
 * 最后写入的非NULL对象
 */
@Objective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LASTOBJECT", displayName = "最后写入的非NULL对象")
public class LastObjectAggregateFunction extends AbstractLastAggregateFunction<Map<String, Object>> {
}
