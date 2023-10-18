package com.yanggu.metric_calculate.core.aggregate_function.map;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MapType;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.List;
import java.util.Map;


/**
 * 对ValeOUT进行排序的map, 并进行limit
 * <p>输出的是{@code List<ValueOUT>}, 只是根据ValueOUT进行排序，取TopN</p>
 *
 * @param <K>
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUERETURNVALUEMAP", displayName = "TOPN值")
public class SortValueReturnValueListMapAggregateFunction<V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMultiFieldDataValueOutComparableMapAggregateFunction<V, ValueACC, ValueOUT, List<ValueOUT>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public List<ValueOUT> getResult(Map<MultiFieldData, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, limit)
                .map(Pair::getRight)
                .toList();
    }

}
