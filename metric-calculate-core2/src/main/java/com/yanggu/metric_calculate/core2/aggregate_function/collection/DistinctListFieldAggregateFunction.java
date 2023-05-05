package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

/**
 * 去重字段列表
 *
 * @param <T>
 */
@MergeType(value = "DISTINCTLISTFIELD")
@Collective(useDistinctField = true, retainObject = false)
public class DistinctListFieldAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
