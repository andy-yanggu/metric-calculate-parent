package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的仍然是{@code Map<List<Object>, ValueOUT>}, 只是根据ValueOUT进行排序，取TopN</p>
 *
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUEMAP", displayName = "TOPN映射")
public class SortValueReturnMapMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMultiFieldDataValueOutComparableMapAggregateFunction<V, ValueACC, ValueOUT, Map<List<Object>, ValueOUT>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public Map<List<Object>, ValueOUT> getResult(Map<MultiFieldData, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, limit)
                .collect(Collectors.toMap(entry -> entry.getLeft().getFieldList(), Pair::getRight, (k1, k2) -> k1, LinkedHashMap::new));
    }

}
