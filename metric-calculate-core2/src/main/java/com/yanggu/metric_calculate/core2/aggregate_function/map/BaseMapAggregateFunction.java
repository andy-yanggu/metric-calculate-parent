package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.annotation.MapType;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import lombok.Data;

import java.util.Map;

/**
 *
 * @param <K> map的key类型
 * @param <V> map的value类型
 * @param <ValueACC> value的累加器类型
 * @param <ValeOUT> value的输出类型
 */
@Data
@MapType
@MergeType("BASEMAP")
public class BaseMapAggregateFunction<K, V, ValueACC, ValeOUT>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, Map<K, ValeOUT>> {

}
