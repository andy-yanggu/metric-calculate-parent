package com.yanggu.metric_calculate.core2.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.*;

/**
 * CEP类型
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class PatternTable<IN, ACC, OUT> extends AbstractTable<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private List<NodePattern> nodePatternList;

    private TimeBaselineDimension timeBaselineDimension;

    private TreeMap<NodePattern, FilterFieldProcessor> filterFieldProcessorMap;

    private TimeFieldProcessor timeFieldProcessor;

    private Long timestamp;

    private TreeMap<NodePattern, TreeMap<Long, IN>> dataMap = new TreeMap<>();

    @Override
    public void init() {
        //初始化过滤条件
        TreeMap<NodePattern, FilterFieldProcessor> tempFilterFieldProcessorMap = new TreeMap<>();

        for (NodePattern node : nodePatternList) {
            FilterFieldProcessor filterFieldProcessor =
                    FieldProcessorUtil.getFilterFieldProcessor(fieldMap, node.getMatchExpress());
            tempFilterFieldProcessorMap.put(node, filterFieldProcessor);
        }
        this.filterFieldProcessorMap = tempFilterFieldProcessorMap;
    }

    @Override
    public void put(JSONObject input) {
        Long tempTimestamp = timeFieldProcessor.process(input);
        filterFieldProcessorMap.forEach((nodePattern, filterProcessor) -> {
            Boolean process = filterProcessor.process(input);
            if (Boolean.TRUE.equals(process)) {
                TreeMap<Long, IN> treeMap = dataMap.computeIfAbsent(nodePattern, key -> new TreeMap<>());
                treeMap.put(tempTimestamp, aggregateFieldProcessor.process(input));
            }
        });
        this.timestamp = tempTimestamp;
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        return query(timestamp);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        Long process = timeFieldProcessor.process(input);
        return query(process);
    }

    //@Override
    public PatternTable<IN, ACC, OUT> merge(PatternTable<IN, ACC, OUT> thatTable) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(dataMap);
    }

    public DeriveMetricCalculateResult<OUT> query(Long timestamp) {
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        TimeWindow timeWindow = timeWindowList.get(0);
        long from = timeWindow.getWindowStart();
        boolean fromInclusive = true;
        long to = timeWindow.getWindowEnd();
        boolean toInclusive = false;

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

        Iterator<NodePattern> iterator = nodePatternList.iterator();
        iterator.next();
        NodePattern nextNode;

        //从第一个节点开始遍历
        while (iterator.hasNext()) {
            nextNode = iterator.next();
            Long size = nextNode.getInterval();
            TreeMap<Long, IN> nextNodeTable = dataMap.get(nextNode);
            nextTable = new TreeMap<>();
            for (Map.Entry<Long, IN> entry : nodeTable.entrySet()) {
                Long tempTimestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subMap(tempTimestamp, false, Math.min(tempTimestamp + size, to), true));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (tempTimestamp + size > nextNodeTable.lastKey()) {
                    break;
                }
            }
            nodeTable = nextTable;
        }

        if (nextTable == null || nextTable.values().isEmpty()) {
            return null;
        }

        //创建累加器
        ACC acc = aggregateFieldProcessor.createAcc();

        //放入明细数据进行累加
        for (IN in : nextTable.values()) {
            acc = aggregateFieldProcessor.add(acc, in);
        }

        //从累加器中获取数据
        OUT out = aggregateFieldProcessor.getOutFromAcc(acc);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(out);
        return deriveMetricCalculateResult;
    }

}
