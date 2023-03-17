package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AbstractAggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;

import java.util.Collection;
import java.util.TreeMap;


public class TimeTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    private final TreeMap<Long, ACC> treeMap = new TreeMap<>();

    public TimeTable(AbstractAggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor, TimeBaselineDimension timeBaselineDimension) {
        super(aggregateFieldProcessor, timeBaselineDimension);
    }

    public void put(Long timestamp, IN in) {
        Long aggregateTimestamp = timeBaselineDimension.getCurrentAggregateTimestamp(timestamp);
        ACC historyAcc = treeMap.get(aggregateTimestamp);
        ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
        treeMap.put(aggregateTimestamp, nowAcc);
    }

    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        Collection<ACC> values = treeMap.subMap(from, fromInclusive, to, toInclusive).values();
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        return aggregateFieldProcessor.getMergeResult(values);
    }

}
