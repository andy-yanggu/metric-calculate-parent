package com.yanggu.metric_calculate.core2.unit.collection;


import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 去重列表
 *
 * @param <T>
 */
public class DistinctListFunction<T> implements AggregateFunction<T, Set<T>, List<T>> {

    @Override
    public Set<T> createAccumulator() {
        return new HashSet<>();
    }

    @Override
    public Set<T> add(T value, Set<T> accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public List<T> getResult(Set<T> accumulator) {
        return new ArrayList<>(accumulator);
    }

    @Override
    public Set<T> merge(Set<T> a, Set<T> b) {
        a.addAll(b);
        return a;
    }

}
