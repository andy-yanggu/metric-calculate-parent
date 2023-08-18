package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的仍然是{@code Map<K, ValueOut>}, 只是根据value进行排序，取TopN</p>
 *
 * @param <K> map的k类型
 * @param <V> map的v类型
 * @param <ValueACC>
 * @param <ValeOUT>
 */
@Data
@MapType
@MergeType("SORTVALUEMAP")
@EqualsAndHashCode(callSuper=false)
public class SortValueMapAggregateFunction<K, V, ValueACC, ValeOUT extends Comparable<ValeOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, Map<K, ValeOUT>> {

    private Integer limit = 10;

    private Boolean asc = true;

    @Override
    public Map<K, ValeOUT> getResult(Map<K, ValueACC> accumulator) {
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
                .limit(limit)
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue, (k1, k2) -> k1, LinkedHashMap::new));
    }

}
