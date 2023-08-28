package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最先写入的非NULL字段
 *
 * @param <T>
 */
@MergeType(value = "FIRSTFIELD", displayName = "最先写入的非NULL字段")
@Objective(keyStrategy = 0, retainStrategy = 1)
public class FirstFieldAggregateFunction<T> extends FirstObjectAggregateFunction<T> {
}
