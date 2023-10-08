package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.Map;


/**
 * 对ValeOUT进行排序, 并且只取第一个K
 * <p>输出的是{@code K}, 只是根据ValeOUT进行排序，取第一个K</p>
 *
 * @param <K>
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUERETURNONEKY", displayName = "TOP1主键")
public class SortValueReturnOneKeyAggregateFunction<K, V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, K> {

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public K getResult(Map<K, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, 1)
                .map(AbstractMap.SimpleImmutableEntry::getKey)
                .findFirst()
                .orElseThrow();
    }

}
