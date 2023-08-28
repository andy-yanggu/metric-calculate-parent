package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

/**
 * 去重字段列表
 *
 * @param <T>
 */
@MergeType(value = "DISTINCTLISTFIELD", displayName = "去重字段列表")
@Collective(keyStrategy = 1, retainStrategy = 1)
public class DistinctListFieldAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
