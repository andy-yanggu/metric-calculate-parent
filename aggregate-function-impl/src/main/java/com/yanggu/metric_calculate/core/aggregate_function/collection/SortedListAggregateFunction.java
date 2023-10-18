package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.util.List;

/**
 * 有序列表
 */
@Collective(keyStrategy = 2, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLIST", displayName = "有序列表")
public class SortedListAggregateFunction extends AbstractMultiFieldSortedListAggregateFunction<Void, List<Object>> {

    @Override
    public List<Object> inToOut(Pair<MultiFieldData, Void> pair) {
        return pair.getLeft().getFieldList();
    }

}
