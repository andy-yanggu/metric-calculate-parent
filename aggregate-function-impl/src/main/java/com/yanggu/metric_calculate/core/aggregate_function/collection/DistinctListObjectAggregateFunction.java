package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.MergeType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 去重对象列表
 *
 * @param <T>
 */
@MergeType("DISTINCTLISTOBJECT")
@Collective(keyStrategy = 1, retainStrategy = 2)
public class DistinctListObjectAggregateFunction<T> extends AbstractCollectionFunction<T, Set<T>, List<T>> {

    @Override
    public Set<T> createAccumulator() {
        return new HashSet<>();
    }

    @Override
    public List<T> getResult(Set<T> accumulator) {
        return new ArrayList<>(accumulator);
    }

}
