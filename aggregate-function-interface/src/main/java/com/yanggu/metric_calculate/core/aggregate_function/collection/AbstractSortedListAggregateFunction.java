package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 有序列表抽象类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractSortedListAggregateFunction<IN, OUT> extends AbstractCollectionFunction<IN, BoundedPriorityQueue<IN>, List<OUT>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度", notNull = true)
    private Integer limit = 10;

    /**
     * IN变成OUT
     *
     * @param in
     * @return
     */
    public abstract OUT inToOut(IN in);

    @Override
    public List<OUT> getResult(BoundedPriorityQueue<IN> accumulator) {
        return accumulator.toList().stream()
                .map(this::inToOut)
                .toList();
    }

}
