package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

/**
 *
 * @param <K> rowKey
 * @param <R>
 * @param <C> column
 * @param <V>
 * @param <T> MergedUnit
 */
public interface Table<K, R extends Value<?>, C, V, T extends Table<K, R, C, V, T>> extends MergedUnit<T> {

    /**
     * Put the {@param column} {@param value} in row that from table by {@param rowKey}, and return the value.
     */
    V putValue(K rowKey, C column, V value);

    /**
     * Clone empty table for this table.
     */
    Table<K, R, C, V, T> cloneEmpty();

    Value query(K from, boolean fromInclusive, K to, boolean toInclusive);

    @Override
    T merge(T that);

    @Override
    T fastClone();

}