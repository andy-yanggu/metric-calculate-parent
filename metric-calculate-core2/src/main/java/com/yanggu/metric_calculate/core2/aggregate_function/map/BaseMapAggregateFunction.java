package com.yanggu.metric_calculate.core2.aggregate_function.map;

import com.yanggu.metric_calculate.core2.annotation.MapType;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.Map;

@MapType
@MergeType("BASEMAP")
public class BaseMapAggregateFunction<K, V, ValueACC, ValeOUT> extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, Map<K, ValeOUT>> {

}
