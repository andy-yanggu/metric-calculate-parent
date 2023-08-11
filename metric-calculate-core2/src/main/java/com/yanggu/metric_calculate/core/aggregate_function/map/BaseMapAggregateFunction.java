package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 基础映射，不对key和value进行任何处理
 *
 * @param <K> map的key类型
 * @param <V> map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValeOUT> value的输出类型
 */
@Data
@MapType
@MergeType("BASEMAP")
@EqualsAndHashCode(callSuper=false)
public class BaseMapAggregateFunction<K, V, ValueACC, ValeOUT>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, Map<K, ValeOUT>> {
}
