package com.yanggu.metric_calculate.core2.unit.collection;


import com.yanggu.metric_calculate.core2.KeyValue;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;

import java.util.HashSet;
import java.util.Set;

public class DistinctListFunction<T> implements AggregateFunction<KeyValue<MultiFieldDistinctKey, T>,
        Set<KeyValue<MultiFieldDistinctKey, T>>, Set<KeyValue<MultiFieldDistinctKey, T>>> {

    @Override
    public Set<KeyValue<MultiFieldDistinctKey, T>> createAccumulator() {
        return new HashSet<>();
    }

    @Override
    public Set<KeyValue<MultiFieldDistinctKey, T>> add(KeyValue<MultiFieldDistinctKey, T> value, Set<KeyValue<MultiFieldDistinctKey, T>> accumulator) {
        accumulator.add(value);
        return accumulator;
    }

    @Override
    public Set<KeyValue<MultiFieldDistinctKey, T>> getResult(Set<KeyValue<MultiFieldDistinctKey, T>> accumulator) {
        return accumulator;
    }

    @Override
    public Set<KeyValue<MultiFieldDistinctKey, T>> merge(Set<KeyValue<MultiFieldDistinctKey, T>> a, Set<KeyValue<MultiFieldDistinctKey, T>> b) {
        a.addAll(b);
        return a;
    }

}
