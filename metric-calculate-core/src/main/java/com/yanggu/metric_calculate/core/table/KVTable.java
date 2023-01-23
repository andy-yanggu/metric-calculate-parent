package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.value.Value;

public interface KVTable<K, V extends Value<?>, T extends KVTable<K, V, T>> extends Table<K, V, K, V, T> {

    /**
     * Whether this table exist the {@param rowKey}.
     */
    boolean existValue(K key);

    /**
     * Return value from table by {@param key}.
     */
    V getValue(K key);

    /**
     * Put the {@param key} and the {@param value} to this table, and return the value.
     */
    V putValue(K key, V value);

    @Override
    default V putValue(K rowKey, K column, V value) {
        return putValue(rowKey, value);
    }

    /**
     * Remove row from table by {@param key}.
     */
    V removeValue(K key);

}