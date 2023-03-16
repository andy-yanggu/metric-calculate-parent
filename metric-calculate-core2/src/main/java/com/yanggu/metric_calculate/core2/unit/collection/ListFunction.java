package com.yanggu.metric_calculate.core2.unit.collection;

import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.ArrayList;
import java.util.List;


public class ListFunction<T> implements AggregateFunction<T, List<T>, List<T>> {

    @Override
    public List<T> createAccumulator() {
        return new ArrayList<>();
    }

    @Override
    public List<T> add(T value, List<T> accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public List<T> getResult(List<T> accumulator) {
        return accumulator;
    }

    @Override
    public List<T> merge(List<T> thisAccumulator, List<T> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        return thisAccumulator;
    }

}
