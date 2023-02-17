package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.unit.pattern.MatchState;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class PatternTable<T> implements Table<Long, MatchState<Map<String, T>>, Long, MatchState<Map<String, T>>, PatternTable<T>>, Serializable {

    private LinkedHashMap<String, TimeSeriesKVTable> dataMap;

    @Override
    public void putValue(Long rowKey, Long column, MatchState<Map<String, T>> value) {
        Map<String, T> data = value.value();
        data.forEach((nodeName, tempData) -> {
            TimeSeriesKVTable timeSeriesKVTable = dataMap.get(nodeName);
            timeSeriesKVTable.put(rowKey, tempData);
        });
    }

    @Override
    public MatchState<Map<String, T>> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return null;
    }

    @Override
    public PatternTable<T> merge(PatternTable<T> that) {
        return null;
    }

    @Override
    public PatternTable<T> cloneEmpty() {
        return null;
    }

    @Override
    public PatternTable<T> fastClone() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}
