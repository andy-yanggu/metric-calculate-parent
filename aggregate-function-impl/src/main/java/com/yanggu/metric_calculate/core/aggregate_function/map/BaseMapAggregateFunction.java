package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 基础映射，不对key和value进行任何处理
 *
 * @param <V>        map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValueOUT> value的输出类型
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "BASEMAP", displayName = "基础映射")
public class BaseMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<? super ValueOUT>> extends
        BaseAbstractMapAggregateFunction<V, ValueACC, ValueOUT, Map<MultiFieldDistinctKey, ValueOUT>> {
}
