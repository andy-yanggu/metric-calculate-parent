package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

/**
 * 去重列表
 *
 * @param <T>
 */
@MergeType(value = "DISTINCTLIST")
@Collective(keyStrategy = 1, retainStrategy = 0)
public class DistinctListAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
