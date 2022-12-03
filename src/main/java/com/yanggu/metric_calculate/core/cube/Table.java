package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;

public interface Table<K, R extends Value, C, V, T extends Table<K, R, C, V, T>> extends MergedUnit<T>, Iterable {

    /**
     * Whether this table exist the {@param rowKey}.
     */
    boolean existRow(K rowKey);

    /**
     * Whether this table is empty.
     */
    boolean isEmpty();

    /**
     * Return row from table by {@param rowKey}.
     */
    R getRow(K rowKey);

    /**
     * Put the {@param rowKey} and the {@param row} to this table, and return the row.
     */
    R putRow(K rowKey, R row);

    /**
     * Return the {@param column} value in row that from table by {@param rowKey}.
     */
    V getValue(K rowKey, C column);

    /**
     * Put the {@param column} {@param value} in row that from table by {@param rowKey}, and return the value.
     */
    V putValue(K rowKey, C column, V value);

    /**
     * Remove row from table by {@param rowKey}.
     */
    R removeRow(K rowKey);

    /**
     * Returns the count of rows in this table.
     */
    long count();

    /**
     * Remove all rows in this table.
     */
    void truncate();

    /**
     * Clone empty table for this table.
     */
    Table<K, R, C, V, T> cloneEmpty();
}