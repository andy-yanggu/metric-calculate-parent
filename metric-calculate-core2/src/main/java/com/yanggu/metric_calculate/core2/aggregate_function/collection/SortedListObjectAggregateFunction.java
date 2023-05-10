package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import cn.hutool.core.collection.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;
import lombok.Data;

import java.util.List;

@Data
@MergeType("SORTEDLIMITLISTOBJECT")
@Collective(useSortedField = true)
public class SortedListObjectAggregateFunction<T extends Comparable<T>> extends AbstractCollectionFunction<T, BoundedPriorityQueue<T>, List<T>> {

    private Integer limit = 10;

    @Override
    public BoundedPriorityQueue<T> createAccumulator() {
        return new BoundedPriorityQueue<>(limit);
    }

    @Override
    public List<T> getResult(BoundedPriorityQueue<T> accumulator) {
        return accumulator.toList();
    }

    @Override
    public BoundedPriorityQueue<T> merge(BoundedPriorityQueue<T> thisAccumulator,
                                         BoundedPriorityQueue<T> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        return thisAccumulator;
    }

}
