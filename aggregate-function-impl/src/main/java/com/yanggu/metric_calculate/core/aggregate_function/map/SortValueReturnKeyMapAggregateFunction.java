package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.Comparator;
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
 * @param <ValeOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper=false)
@MergeType("SORTVALUERETURNKEYMAP")
public class SortValueReturnKeyMapAggregateFunction<K, V, ValueACC, ValeOUT extends Comparable<ValeOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, List<K>> {

    private Integer limit = 10;

    private Boolean asc = true;

    @Override
    public List<K> getResult(Map<K, ValueACC> accumulator) {
        Comparator<AbstractMap.SimpleImmutableEntry<K, ValeOUT>> pairComparator = (o1, o2) -> {
            if (Boolean.TRUE.equals(asc)) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        };
        return accumulator.entrySet().stream()
                .map(tempEntry -> new AbstractMap.SimpleImmutableEntry<>(tempEntry.getKey(), valueAggregateFunction.getResult(tempEntry.getValue())))
                .sorted(pairComparator)
                .map(AbstractMap.SimpleImmutableEntry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
    }

}
