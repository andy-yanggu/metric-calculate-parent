package com.yanggu.metric_calculate.core2.aggregate_function.map;

import cn.hutool.core.lang.Pair;
import com.yanggu.metric_calculate.core2.annotation.MapType;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的仍然是{@code Map<K, ValueOut>}, 只是根据value进行排序，取TopN</p>
 *
 * @param <K>
 * @param <V>
 * @param <ValueACC>
 * @param <ValeOUT>
 */
@MapType
@MergeType("SORTVALUEMAP")
public class SortValueMapAggregateFunction<K, V, ValueACC, ValeOUT extends Comparable<ValeOUT>>
        extends AbstractMapAggregateFunction<K, V, ValueACC, ValeOUT, Map<K, ValeOUT>> {

    private Integer limit = 10;

    private Boolean asc = true;

    @Override
    public Map<K, ValeOUT> getResult(Map<K, ValueACC> accumulator) {
        Comparator<Pair<K, ValeOUT>> pairComparator = (o1, o2) -> {
            if (Boolean.TRUE.equals(asc)) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        };
        return accumulator.entrySet().stream()
                .map(tempEntry -> Pair.of(tempEntry.getKey(), valueAggregateFunction.getResult(tempEntry.getValue())))
                .sorted(pairComparator)
                .limit(limit)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (k1, k2) -> k1, LinkedHashMap::new));
    }

}
