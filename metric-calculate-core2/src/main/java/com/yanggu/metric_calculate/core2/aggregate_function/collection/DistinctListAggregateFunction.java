package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

/**
 * 去重列表
 *
 * @param <T>
 */
@MergeType(value = "DISTINCTLIST")
@Collective(useDistinctField = true, useSortedField = false, retainStrategy = 0)
public class DistinctListAggregateFunction<T> extends DistinctListObjectAggregateFunction<T> {
}
