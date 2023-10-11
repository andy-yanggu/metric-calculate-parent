package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
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
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSortedListAggregateFunction<IN, OUT> extends
        AbstractCollectionFunction<IN, BoundedPriorityQueue<IN>, List<OUT>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度", notNull = true)
    private Integer limit = 10;

    @Override
    public BoundedPriorityQueue<IN> createAccumulator() {
        return new BoundedPriorityQueue<>(limit);
    }

    @Override
    public List<OUT> getResult(BoundedPriorityQueue<IN> accumulator) {
        return accumulator.toList().stream()
                .map(this::inToOut)
                .toList();
    }

    public abstract OUT inToOut(IN in);

}
