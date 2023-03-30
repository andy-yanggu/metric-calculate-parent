package com.yanggu.metric_calculate.core2.aggregate_function.collection;

import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Collective
@MergeType("LISTOBJECT")
public class ListObjectFunction<T> implements AggregateFunction<T, List<T>, List<T>> {

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
