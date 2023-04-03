package com.yanggu.metric_calculate.core2.table;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class PatternTable<IN, ACC, OUT> implements Table<JSONObject, ACC, OUT> {

    private TreeMap<NodePattern, FilterFieldProcessor> filterFieldProcessorMap;

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private TreeMap<NodePattern, TreeMap<Long, IN>> dataMap = new TreeMap<>();

    @Override
    public void put(Long timestamp, JSONObject in) {
        filterFieldProcessorMap.forEach((nodePattern, filterProcessor) -> {
            Boolean process = filterProcessor.process(in);
            if (Boolean.TRUE.equals(process)) {
                TreeMap<Long, IN> treeMap = dataMap.computeIfAbsent(nodePattern, key -> new TreeMap<>());
                treeMap.put(timestamp, aggregateFieldProcessor.process(in));
            }
        });
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {

        //判断最后一个节点是否有数据
        NavigableMap<Long, IN> endTable = dataMap.lastEntry().getValue()
                .subMap(from, fromInclusive, to, toInclusive);
        if (endTable.isEmpty()) {
            return null;
        }

        //从第一个节点进行截取数据
        NavigableMap<Long, IN> nodeTable = dataMap.firstEntry().getValue()
                .subMap(from, fromInclusive, to, toInclusive);

        //判断第一个节点是否有数据
        if (nodeTable.isEmpty()) {
            return null;
        }

        TreeMap<Long, IN> nextTable = null;

        Iterator<NodePattern> iterator = dataMap.keySet().iterator();
        iterator.next();
        NodePattern nextNode;

        while (iterator.hasNext()) {
            nextNode = iterator.next();
            Long size = nextNode.getInterval();
            TreeMap<Long, IN> nextNodeTable = dataMap.get(nextNode);
            nextTable = new TreeMap<>();
            for (Map.Entry<Long, IN> entry : nodeTable.entrySet()) {
                Long timestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subMap(timestamp, false, Math.min(timestamp + size, to), true));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (timestamp + size > nextNodeTable.lastKey()) {
                    break;
                }
            }
            nodeTable = nextTable;
        }

        if (nextTable == null || nextTable.values().isEmpty()) {
            return null;
        }

        ACC acc = aggregateFieldProcessor.createAcc();

        for (IN in : nextTable.values()) {
            acc = aggregateFieldProcessor.add(acc, in);
        }

        return aggregateFieldProcessor.getOutFromAcc(acc);
    }

}
