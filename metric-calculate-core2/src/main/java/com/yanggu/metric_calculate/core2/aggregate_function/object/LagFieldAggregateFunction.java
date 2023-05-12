package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

@MergeType("LAGFIELD")
@Objective(useCompareField = false, retainObject = false)
public class LagFieldAggregateFunction<IN> extends LagObjectAggregateFunction<IN> {
}
