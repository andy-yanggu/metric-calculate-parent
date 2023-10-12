package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的是{@code List<K>}, 只是根据value进行排序，取对应K的TopN</p>
 *
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUERETURNKEYMAP", displayName = "TOPN主键")
public class SortValueReturnKeyMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends BaseAbstractMapAggregateFunction<V, ValueACC, ValueOUT, List<List<Object>>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public List<List<Object>> getResult(Map<MultiFieldDistinctKey, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, limit)
                .map(AbstractMap.SimpleImmutableEntry::getKey)
                .map(MultiFieldDistinctKey::getFieldList)
                .toList();
    }

}
