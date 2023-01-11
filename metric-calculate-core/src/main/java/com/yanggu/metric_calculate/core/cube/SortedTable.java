package com.yanggu.metric_calculate.core.cube;

import com.yanggu.metric_calculate.core.value.Value;

public interface SortedTable<K extends Comparable<K>, R extends Value, C, V, T extends SortedTable<K, R, C, V, T>>
        extends Table<K, R, C, V, T> {

    /**
     * Query value between {@param from} to {@param to}.
     */
    Value query(K from, K to);

    /**
     * Return first row key.
     */
    K firstKey();

    /**
     * Return last row key.
     */
    K lastKey();

    /**
     * Return first row.
     */
    @Deprecated
    default R fistRow() {
        return firstRow();
    }

    /**
     * Return first row.
     */
    R firstRow();

    /**
     * Return last row.
     */
    R lastRow();

    /**
     * Return first info.
     */
    @Deprecated
    default Value fist() {
        return first();
    }

    /**
     * Return first info.
     */
    Value first();

    /**
     * Return last info.
     */
    Value last();

    /**
     * Return sub table from {@param from} to {@param to}.
     */
    T subTable(K from, K to);

    T subTable(K from, boolean fromInclusive, K to, boolean toInclusive);

}