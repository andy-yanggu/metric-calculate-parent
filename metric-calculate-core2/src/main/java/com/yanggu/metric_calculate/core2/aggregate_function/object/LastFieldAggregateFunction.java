package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最后写入的非NULL值
 *
 * @param <T>
 */
@MergeType("LASTFIELD")
@Objective(useCompareField = false, retainObject = false)
public class LastFieldAggregateFunction<T> extends LastObjectAggregateFunction<T> {
}
