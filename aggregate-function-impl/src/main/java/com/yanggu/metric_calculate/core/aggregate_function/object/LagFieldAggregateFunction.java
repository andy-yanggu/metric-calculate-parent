package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 当前行的第前N个字段
 *
 * @param <IN>
 */
@MergeType(value = "LAGFIELD", displayName = "当前行的第前N个字段")
@Objective(keyStrategy = 0, retainStrategy = 1)
public class LagFieldAggregateFunction<IN> extends LagObjectAggregateFunction<IN> {
}
