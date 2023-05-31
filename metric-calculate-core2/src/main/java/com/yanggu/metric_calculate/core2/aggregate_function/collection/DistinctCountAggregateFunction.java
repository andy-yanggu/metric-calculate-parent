package com.yanggu.metric_calculate.core2.aggregate_function.collection;


import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.Collective;
import com.yanggu.metric_calculate.core2.annotation.MergeType;

import java.util.HashSet;
import java.util.Set;

/**
 * 去重计数
 *
 * @param <T>
 */
@MergeType("DISTINCTCOUNT")
@Collective(useDistinctField = true, useSortedField = false, retainStrategy = 0)
public class DistinctCountAggregateFunction<T> implements AggregateFunction<T, Set<T>, Integer> {

    @Override
    public Set<T> createAccumulator() {
        return new HashSet<>();
    }

    @Override
    public Set<T> add(T input, Set<T> accumulator) {
        accumulator.add(input);
        return accumulator;
    }

    @Override
    public Integer getResult(Set<T> accumulator) {
        return accumulator.size();
    }

    @Override
    public Set<T> merge(Set<T> thisAccumulator, Set<T> thatAccumulator) {
        thisAccumulator.addAll(thatAccumulator);
        return thisAccumulator;
    }

}
