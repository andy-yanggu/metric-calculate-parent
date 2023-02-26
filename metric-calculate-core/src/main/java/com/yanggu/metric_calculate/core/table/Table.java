package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.Mergeable;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.Value;

/**
 * 底层存储MergedUnit数据结构
 *
 * @param <K> windowStartTime
 * @param <R> 查询得到的MeredUnit
 * @param <C> windowEndTime
 * @param <V> 传入的MergedUnit
 * @param <T> 实现Table类型
 */
public interface Table<K, R extends Value<?>, C, V, T extends Table<K, R, C, V, T>> extends Mergeable<T>, Clone<T> {

    void putValue(K rowKey, C column, V value);

    R query(K from, boolean fromInclusive, K to, boolean toInclusive);

    @Override
    T merge(T that);

    T cloneEmpty();

    @Override
    T fastClone();

    boolean isEmpty();

}