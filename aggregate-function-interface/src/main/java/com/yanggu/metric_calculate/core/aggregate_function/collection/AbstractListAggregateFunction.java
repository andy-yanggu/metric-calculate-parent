package com.yanggu.metric_calculate.core.aggregate_function.collection;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.AggregateFunctionFieldAnnotation;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 列表聚合函数抽象类
 * <p>子类添加响应泛型即可</p>
 *
 * @param <IN>
 */
@Data
public abstract class AbstractListAggregateFunction<IN> implements AggregateFunction<IN, List<IN>, List<IN>> {

    @AggregateFunctionFieldAnnotation(displayName = "长度限制")
    private Integer limit = 10;

    @Override
    public List<IN> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<IN> add(IN input, List<IN> accumulator) {
        if (limit > accumulator.size()) {
            accumulator.add(input);
        }
        return accumulator;
    }

    @Override
    public List<IN> getResult(List<IN> accumulator) {
        return accumulator;
    }

    @Override
    public List<IN> merge(List<IN> thisAccumulator, List<IN> thatAccumulator) {
        Iterator<IN> iterator = thatAccumulator.iterator();
        while (limit > thisAccumulator.size() && iterator.hasNext()) {
            thisAccumulator.add(iterator.next());
        }
        return thisAccumulator;
    }

}
