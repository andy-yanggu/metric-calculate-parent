package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.Map;


/**
 * 对ValeOUT进行排序的map, 取首个
 * <p>输出的是{@code List<ValueACC>}, 只是根据value进行排序，取首个</p>
 *
 * @param <K>
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUERETURNONEVALUEMAP", displayName = "TOP1值")
public class SortValueReturnOneValueMapAggregateFunction<K, V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, ValueOUT> {

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public ValueOUT getResult(Map<K, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, 1)
                .map(AbstractMap.SimpleImmutableEntry::getValue)
                .findFirst()
                .orElseThrow();
    }

}
