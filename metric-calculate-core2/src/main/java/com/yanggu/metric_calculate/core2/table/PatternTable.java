package com.yanggu.metric_calculate.core2.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.pattern.PatternAggregateFunction;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.ChainPattern;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class PatternTable<OUT> implements Table<JSONObject, TreeMap<NodePattern, TreeMap<Long, JSONObject>>, OUT> {

    private PatternAggregateFunction patternAggregateFunction;

    private TreeMap<NodePattern, TreeMap<Long, JSONObject>> dataMap = new TreeMap<>();

    @Override
    public void put(Long timestamp, JSONObject in) {
        dataMap = patternAggregateFunction.add(Pair.of(timestamp, in), dataMap);
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        dataMap = patternAggregateFunction.getResult(dataMap);
        //判断最后一个节点是否有数据
        NavigableMap<Long, JSONObject> endTable = dataMap.lastEntry().getValue()
                .subMap(from, fromInclusive, to, toInclusive);
        if (endTable.isEmpty()) {
            return null;
        }

        //从第一个节点进行截取数据
        NavigableMap<Long, JSONObject> nodeTable = dataMap.firstEntry().getValue()
                .subMap(from, fromInclusive, to, toInclusive);

        //判断第一个节点是否有数据
        if (nodeTable.isEmpty()) {
            return null;
        }

        TreeMap<Long, JSONObject> nextTable = null;

        Iterator<NodePattern> iterator = dataMap.keySet().iterator();
        iterator.next();
        NodePattern nextNode;

        while (iterator.hasNext()) {
            nextNode = iterator.next();
            Long size = nextNode.getInterval();
            TreeMap<Long, JSONObject> nextNodeTable = dataMap.get(nextNode);
            nextTable = new TreeMap<>();
            for (Map.Entry<Long, JSONObject> entry : nodeTable.entrySet()) {
                Long timestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subMap(timestamp, false,Math.min(timestamp + size, to), true));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (timestamp + size > nextNodeTable.lastKey()) {
                    break;
                }
            }
            nodeTable = nextTable;
        }

        if (CollUtil.isEmpty(nextTable)) {
            return null;
        }
        return null;
    }

}
