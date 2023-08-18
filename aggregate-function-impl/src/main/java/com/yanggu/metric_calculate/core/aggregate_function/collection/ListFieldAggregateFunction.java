package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

/**
 * 字段列表
 *
 * @param <T>
 */
@MergeType("LISTFIELD")
@Collective(keyStrategy = 0, retainStrategy = 1)
public class ListFieldAggregateFunction<T> extends ListObjectAggregateFunction<T> {
}
