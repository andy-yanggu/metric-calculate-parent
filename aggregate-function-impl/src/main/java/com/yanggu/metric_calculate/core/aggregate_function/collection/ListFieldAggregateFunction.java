package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;

/**
 * 字段列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 0, retainStrategy = 1)
@AggregateFunctionAnnotation(name = "LISTFIELD", displayName = "字段列表")
public class ListFieldAggregateFunction<T> extends AbstractListAggregateFunction<T> {
}
