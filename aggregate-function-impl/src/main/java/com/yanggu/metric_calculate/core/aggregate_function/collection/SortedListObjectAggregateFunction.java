package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.Map;

/**
 * 有序对象列表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Collective(keyStrategy = 2, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLISTOBJECT", displayName = "有序对象列表")
public class SortedListObjectAggregateFunction extends
        AbstractMultiFieldSortedListAggregateFunction<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> inToOut(Pair<MultiFieldData, Map<String, Object>> pair) {
        return pair.getRight();
    }

}
