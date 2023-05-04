package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 占位字段
 *
 * @param <T>
 */
@MergeType("OCCUPIEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class OccupiedFieldAggregateFunction<T> extends OccupiedObjectAggregateFunction<T> {
}
