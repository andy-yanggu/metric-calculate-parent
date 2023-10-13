package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

import java.util.HashSet;
import java.util.Set;

/**
 * 去重聚合函数抽象类
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 *
 * @param <T>   输入数据类型
 * @param <OUT> 输出类型
 */
public abstract class AbstractDistinctAggregateFunction<T, OUT> extends AbstractCollectionFunction<T, Set<T>, OUT> {

    @Override
    public Set<T> createAccumulator() {
        return new HashSet<>();
    }

}
