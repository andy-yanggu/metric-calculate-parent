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
 * 对ValeOUT进行排序, 并且只取第一个K
 * <p>输出的是{@code List<Object>}, 只是根据ValeOUT进行排序，取第一个</p>
 *
 * @param <V>
 * @param <ValueACC>
 * @param <ValueOUT>
 */
@Data
@MapType
@EqualsAndHashCode(callSuper = false)
@AggregateFunctionAnnotation(name = "SORTVALUERETURNONEKY", displayName = "TOP1主键")
public class SortValueReturnOneKeyAggregateFunction<V, ValueACC, ValueOUT extends Comparable<ValueOUT>>
        extends AbstractMultiFieldDataValueOutComparableMapAggregateFunction<V, ValueACC, ValueOUT, List<Object>> {

    @AggregateFunctionFieldAnnotation(displayName = "升降序", description = "true升序，false降序")
    private Boolean asc = true;

    @Override
    public List<Object> getResult(Map<MultiFieldData, ValueACC> accumulator) {
        return getCompareLimitStream(accumulator, asc, 1)
                .map(Pair::getLeft)
                .map(MultiFieldData::getFieldList)
                .findFirst()
                .orElseThrow();
    }

}
