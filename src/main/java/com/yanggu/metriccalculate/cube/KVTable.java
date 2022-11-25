package com.yanggu.metriccalculate.cube;

import com.yanggu.metriccalculate.value.Value;

public interface KVTable<K, V extends Value, T extends KVTable<K, V, T>> extends Table<K, V, K, V, T> {

    /**
     * Whether this table exist the {@param rowKey}.
     */
    boolean existValue(K key);

    /**
     * Return value from table by {@param key}.
     */
    V getValue(K key);

    @Override
    default V getValue(K rowKey, K column) {
        return getValue(rowKey);
    }

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

    @Override
    default boolean existRow(K rowKey) {
        return existValue(rowKey);
    }

    @Override
    default V getRow(K rowKey) {
        return getValue(rowKey);
    }

    @Override
    default V putRow(K rowKey, V row) {
        return putValue(rowKey, row);
    }

    @Override
    default V removeRow(K rowKey) {
        return removeValue(rowKey);
    }
}