package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.pojo.acc.KeyValue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;

import java.util.List;

/**
 * 有序列表
 *
 * @param <T>
 */
@Collective(keyStrategy = 2, retainStrategy = 0)
@AggregateFunctionAnnotation(name = "SORTEDLIMITLIST", displayName = "有序列表")
public class SortedListAggregateFunction extends AbstractSortedListAggregateFunction<MultiFieldOrderCompareKey, List<Object>> {

    @Override
    public List<Object> inToOut(MultiFieldOrderCompareKey multiFieldOrderCompareKey) {
        return multiFieldOrderCompareKey.getDataList();
    }

}
