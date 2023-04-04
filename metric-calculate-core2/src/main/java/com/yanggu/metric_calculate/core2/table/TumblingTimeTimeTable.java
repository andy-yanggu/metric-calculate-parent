package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.Collection;
import java.util.TreeMap;

/**
 * 滚动时间窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class TumblingTimeTimeTable<IN, ACC, OUT> extends TimeTable<IN, ACC, OUT> {

    private TreeMap<Long, ACC> treeMap = new TreeMap<>();

    @Override
    public void put(Long timestamp, IN in) {
        Long aggregateTimestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        ACC historyAcc = treeMap.get(aggregateTimestamp);
        ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
        treeMap.put(aggregateTimestamp, nowAcc);
        super.timestamp = timestamp;
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        Collection<ACC> values = treeMap.subMap(from, fromInclusive, to, toInclusive).values();
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        return aggregateFieldProcessor.getMergeResult(values);
    }

}
