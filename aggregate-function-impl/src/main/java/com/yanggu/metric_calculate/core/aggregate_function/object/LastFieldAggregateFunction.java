package com.yanggu.metric_calculate.core.aggregate_function.object;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;

/**
 * 最后写入的非NULL值
 *
 * @param <T>
 */
@MergeType(value = "LASTFIELD", displayName = "最后写入的非NULL值")
@Objective(keyStrategy = 0, retainStrategy = 1)
public class LastFieldAggregateFunction<T> extends LastObjectAggregateFunction<T> {
}
