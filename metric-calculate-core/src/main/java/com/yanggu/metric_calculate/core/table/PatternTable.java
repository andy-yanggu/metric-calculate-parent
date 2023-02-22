package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.unit.pattern.FinishState;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.unit.pattern.Pattern;
import com.yanggu.metric_calculate.core.unit.pattern.PatternNode;
import com.yanggu.metric_calculate.core.value.*;
import org.springframework.data.redis.connection.ReturnType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class PatternTable<T extends Clone<T>> implements
        Table<Long, FinishState<T>, Long , MatchState<TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>>>, PatternTable<T>> {

    private Pattern pattern;

    private TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, TimeSeriesKVTable<MatchState<T>>> dataMap;

    @Override
    public void putValue(Long startTime, Long endTime, MatchState<TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>>> value) {
        TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>> data = value.value();
        data.forEach((keyValue, tempData) -> {
            TimeSeriesKVTable<MatchState<T>> table = dataMap.get(keyValue);
            table.put(startTime, new MatchState<>(tempData.value()));
        });
    }

    @Override
    public FinishState<T> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        TimeSeriesKVTable<MatchState<T>> endTable = dataMap.lastEntry().getValue().subTable(from, fromInclusive, to, toInclusive);
        if (endTable.isEmpty()) {
            return null;
        }

        MergedUnit result;

        TimeSeriesKVTable<MatchState<T>> nodeTable = dataMap.firstEntry().getValue().subTable(from, fromInclusive, to, toInclusive);
        TimeSeriesKVTable<MatchState<T>> nextTable = null;

        PatternNode node = pattern.getRootNode();
        PatternNode nextNode;

        while ((nextNode = node.getNextNode()) != null) {
            Long size = node.getConnector().getCond().timeBaseline().realLength();
            KeyValue<Key<Integer>, CloneWrapper<String>> keyValue = new KeyValue<>(new Key<>(nextNode.getIndex()), CloneWrapper.wrap(nextNode.getName()));
            TimeSeriesKVTable<MatchState<T>> nextNodeTable = dataMap.get(keyValue);
            nextTable = nextNodeTable.cloneEmpty();
            for (Map.Entry<Long, MatchState<T>> entry : nodeTable.entrySet()) {
                Long timestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subTable(timestamp, true,Math.min(timestamp + size, to), false));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (timestamp + size > nextNodeTable.lastKey()) {
                    break;
                }
            }
            nodeTable = nextTable;
            node = nextNode;
        }

        TreeMap<Long, T> returnDataMap = new TreeMap<>();
        if (nextTable != null) {
            nextTable.forEach((key, value) -> returnDataMap.put(key, value.value()));
        }
        return new FinishState<>(returnDataMap);
    }

    @Override
    public PatternTable<T> merge(PatternTable<T> that) {
        that.dataMap.forEach((nodeName, tempTable) -> dataMap.get(nodeName).merge(tempTable));
        return this;
    }

    @Override
    public PatternTable<T> cloneEmpty() {
        PatternTable<T> patternTable = new PatternTable<>();
        patternTable.dataMap = new TreeMap<>();
        return patternTable;
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
