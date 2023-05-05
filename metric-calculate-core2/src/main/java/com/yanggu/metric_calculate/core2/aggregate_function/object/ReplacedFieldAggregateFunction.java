package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 取代对象
 *
 * @param <T>
 */
@MergeType("REPLACEDFIELD")
@Objective(useCompareField = false, retainObject = false)
public class ReplacedFieldAggregateFunction<T> extends ReplacedObjectAggregateFunction<T> {
}
