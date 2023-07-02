package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Objective;

/**
 * 最小值
 *
 * @param <T>
 */
@MergeType("MINFIELD")
@Objective(keyStrategy = 3, retainStrategy = 1)
public class MinFieldAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
