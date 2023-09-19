package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的仍然是{@code Map<K, ValueOut>}, 只是根据value进行排序，取TopN</p>
 *
 * @param <K>        map的k类型
 * @param <V>        map的v类型
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUEMAP", displayName = "TOPN映射")
public class SortValueMapAggregateFunction<K, V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, Map<K, ValueOUT>> {

    private Integer limit = 10;

    private Boolean asc = true;

    @Override
    public Map<K, ValueOUT> getResult(Map<K, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, limit)
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
    }

}
