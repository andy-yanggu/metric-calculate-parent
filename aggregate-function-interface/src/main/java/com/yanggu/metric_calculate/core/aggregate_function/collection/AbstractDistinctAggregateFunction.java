package com.yanggu.metric_calculate.core.aggregate_function.collection;


import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;

import java.util.HashSet;
import java.util.Set;

/**
 * 去重聚合函数抽象类
 * <p>子类需要重写{@link AggregateFunction#getResult(Object)}方法</p>
 * <p>IN元素需要手动重写hashCode函数和equals方法，实现去重语义</p>
 *
 * @param <IN>  输入数据类型
 * @param <OUT> 输出类型
 */
public abstract class AbstractDistinctAggregateFunction<IN, OUT> extends AbstractCollectionFunction<IN, Set<IN>, OUT> {

    @Override
    public Set<IN> createAccumulator() {
        return new HashSet<>();
    }

}
