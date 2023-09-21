package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 对象列表
 *
 * @param <T>
 */
@Data
@Collective(keyStrategy = 0, retainStrategy = 2)
@AggregateFunctionAnnotation(name = "LISTOBJECT", displayName = "对象列表")
public class ListObjectAggregateFunction<T> implements AggregateFunction<T, List<T>, List<T>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @Override
    public List<T> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<T> add(T input, List<T> accumulator) {
        if (limit > accumulator.size()) {
            accumulator.add(input);
        }
        return accumulator;
    }

    @Override
    public List<T> getResult(List<T> accumulator) {
        return accumulator;
    }

    @Override
    public List<T> merge(List<T> thisAccumulator, List<T> thatAccumulator) {
        Iterator<T> iterator = thatAccumulator.iterator();
        while (limit > thisAccumulator.size() && iterator.hasNext()) {
            thisAccumulator.add(iterator.next());
        }
        return thisAccumulator;
    }

}
