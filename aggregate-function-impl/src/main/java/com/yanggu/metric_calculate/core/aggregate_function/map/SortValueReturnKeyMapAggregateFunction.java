package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的是{@code List<K>}, 只是根据value进行排序，取对应K的TopN</p>
 *
 * @param <K>
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper=false)
@MergeType("SORTVALUERETURNKEYMAP")
public class SortValueReturnKeyMapAggregateFunction<K, V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValueOUT, List<K>> {

    private Integer limit = 10;

    private Boolean asc = true;

    @Override
    public List<K> getResult(Map<K, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, limit)
                .map(AbstractMap.SimpleImmutableEntry::getKey)
                .collect(Collectors.toList());
    }

}
