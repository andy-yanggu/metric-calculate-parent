package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最小值
 *
 * @param <T>
 */
@MergeType("MINVALUE")
@Objective(keyStrategy = 3, retainStrategy = 0)
public class MinValueAggregateFunction<T extends Comparable<T>> extends MinObjectAggregateFunction<T> {
}
