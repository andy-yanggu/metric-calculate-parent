package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

import java.util.HashSet;
import java.util.Set;

/**
 * 去重计数
 *
 * @param <T>
 */
@MergeType(value = "DISTINCTCOUNT", displayName = "去重计数")
@Collective(keyStrategy = 1, retainStrategy = 0)
public class DistinctCountAggregateFunction<T> extends AbstractCollectionFunction<T, Set<T>, Integer> {

    @Override
    public Set<T> createAccumulator() {
        return new HashSet<>();
    }

    @Override
    public Integer getResult(Set<T> accumulator) {
        return accumulator.size();
    }

}
