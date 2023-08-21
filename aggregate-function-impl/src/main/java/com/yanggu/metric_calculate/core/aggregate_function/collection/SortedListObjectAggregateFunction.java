package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;

import java.util.List;

/**
 * 有序对象列表
 *
 * @param <T>
 */
@Data
@MergeType("SORTEDLIMITLISTOBJECT")
@EqualsAndHashCode(callSuper=false)
@Collective(keyStrategy = 2, retainStrategy = 2)
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

}
