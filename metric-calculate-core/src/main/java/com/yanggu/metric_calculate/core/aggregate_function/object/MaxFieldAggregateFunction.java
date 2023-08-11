package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最大字段
 *
 * @param <T>
 */
@MergeType("MAXFIELD")
@Objective(keyStrategy = 3, retainStrategy = 1)
public class MaxFieldAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
