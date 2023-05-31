package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

/**
 * 字段列表
 *
 * @param <T>
 */
@MergeType("LISTFIELD")
@Collective(useSortedField = false, useDistinctField = false, retainStrategy = 1)
public class ListFieldAggregateFunction<T> extends ListObjectAggregateFunction<T> {
}
