package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最先写入的非NULL值
 *
 * @param <T>
 */
@MergeType("FIRSTFIELD")
@Objective(useCompareField = false, retainObject = false)
public class FirstFieldAggregateFunction<T> extends FirstObjectAggregateFunction<T> {
}
