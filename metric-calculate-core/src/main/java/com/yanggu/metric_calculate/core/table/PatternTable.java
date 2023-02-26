package com.yanggu.metric_calculate.core.table;

import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.unit.pattern.FinishState;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.value.Clone;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import lombok.Data;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 输入的明细数据类型
 *
 * @param <T>
 */
@Data
public class PatternTable<T extends Clone<T>> implements
        Table<Long, FinishState<T>, Long , MatchState<TreeMap<NodePattern, CloneWrapper<T>>>, PatternTable<T>> {

    private TreeMap<NodePattern, TimeSeriesKVTable<MatchState<T>>> dataMap;

    @Override
    public void putValue(Long timestamp, Long noneUse, MatchState<TreeMap<NodePattern, CloneWrapper<T>>> value) {
        TreeMap<NodePattern, CloneWrapper<T>> data = value.value();
        data.forEach((keyValue, tempData) -> {
            TimeSeriesKVTable<MatchState<T>> table = dataMap.get(keyValue);
            table.put(timestamp, new MatchState<>(tempData.value()));
        });
    }

    @Override
    public FinishState<T> query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {

        //判断最后一个节点是否有数据
        TimeSeriesKVTable<MatchState<T>> endTable = dataMap.lastEntry().getValue()
                .subTable(from, fromInclusive, to, toInclusive);
        if (endTable.isEmpty()) {
            return null;
        }

        //从第一个节点进行截取数据
        TimeSeriesKVTable<MatchState<T>> nodeTable = dataMap.firstEntry().getValue()
                .subTable(from, fromInclusive, to, toInclusive);

        //判断第一个节点是否有数据
        if (nodeTable.isEmpty()) {
            return null;
        }

        TimeSeriesKVTable<MatchState<T>> nextTable = null;

        Iterator<NodePattern> iterator = dataMap.keySet().iterator();
        iterator.next();
        NodePattern nextNode;

        while (iterator.hasNext()) {
            nextNode = iterator.next();
            Long size = nextNode.getInterval();
            TimeSeriesKVTable<MatchState<T>> nextNodeTable = dataMap.get(nextNode);
            nextTable = nextNodeTable.cloneEmpty();
            for (Map.Entry<Long, MatchState<T>> entry : nodeTable.entrySet()) {
                Long timestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subTable(timestamp, false,Math.min(timestamp + size, to), true));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (timestamp + size > nextNodeTable.lastKey()) {
                    break;
                }
            }
            nodeTable = nextTable;
        }

        TreeMap<Long, T> returnDataMap = new TreeMap<>();
        if (nextTable != null && !nextTable.isEmpty()) {
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
        PatternTable<T> patternTable = cloneEmpty();
        TreeMap<NodePattern, TimeSeriesKVTable<MatchState<T>>> newDataMap = new TreeMap<>();
        this.dataMap.forEach((k, v) -> newDataMap.put(k, v.fastClone()));
        patternTable.dataMap = newDataMap;
        return patternTable;
    }

    @Override
    public boolean isEmpty() {
        return dataMap.values().stream().allMatch(TimeSeriesKVTable::isEmpty);
    }

}
