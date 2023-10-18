package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;

/**
 * 只是定义了K的泛型为MultiFieldDistinctKey
 *
 * @param <V>        map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValueOUT> value的输出类型
 * @param <OUT>      输出数据类型
 */
public abstract class AbstractMultiFieldDistinctKeyMapAggregateFunction<V, ValueACC, ValueOUT, OUT>
        extends AbstractMapAggregateFunction<MultiFieldDistinctKey, V, ValueACC, ValueOUT, OUT> {
}
