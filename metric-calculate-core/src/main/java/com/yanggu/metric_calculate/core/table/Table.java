package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

/**
 *
 * @param <K> rowKey
 * @param <R> 查询得到的数据MeredUnit
 * @param <C> column
 * @param <V> MergedUnit
 * @param <T> 实现Table类型
 */
public interface Table<K, R extends Value<?>, C, V, T extends Table<K, R, C, V, T>> extends MergedUnit<T> {

    /**
     * Put the {@param column} {@param value} in row that from table by {@param rowKey}, and return the value.
     */
    V putValue(K rowKey, C column, V value);

    R query(K from, boolean fromInclusive, K to, boolean toInclusive);

    @Override
    T merge(T that);

    /**
     * Clone empty table for this table.
     */
    Table<K, R, C, V, T> cloneEmpty();

    @Override
    T fastClone();

    boolean isEmpty();

}