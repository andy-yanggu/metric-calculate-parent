package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最先写入的非NULL值
 *
 * @param <T>
 */
@MergeType("FIRSTFIELD")
@Objective(keyStrategy = 0, retainStrategy = 1)
public class FirstFieldAggregateFunction<T> extends FirstObjectAggregateFunction<T> {
}
