package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

@MergeType("LAGFIELD")
@Objective(keyStrategy = 0, retainStrategy = 1)
public class LagFieldAggregateFunction<IN> extends LagObjectAggregateFunction<IN> {
}
