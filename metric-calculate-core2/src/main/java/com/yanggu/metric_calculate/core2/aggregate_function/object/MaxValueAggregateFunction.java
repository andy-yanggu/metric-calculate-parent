package com.yanggu.metric_calculate.core2.aggregate_function.object;

import com.yanggu.metric_calculate.core2.annotation.MergeType;
import com.yanggu.metric_calculate.core2.annotation.Objective;

/**
 * 最大值
 *
 * @param <T>
 */
@MergeType("MAXVALUE")
@Objective(keyStrategy = 3, retainStrategy = 0)
public class MaxValueAggregateFunction<T extends Comparable<T>> extends MaxObjectAggregateFunction<T> {
}
