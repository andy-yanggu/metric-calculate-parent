package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import org.dromara.hutool.json.JSONObject;

/**
 * 最先写入的非NULL对象
 */
@Objective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "FIRSTOBJECT", displayName = "最先写入的非NULL对象")
public class FirstObjectAggregateFunction extends AbstractFirstAggregateFunction<JSONObject> {
}
