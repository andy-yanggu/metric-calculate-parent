package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.ArrayList;
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
public class TumblingTimeTable<IN, ACC, OUT> extends TimeTable<IN, ACC, OUT, TumblingTimeTable<IN, ACC, OUT>> {

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
        return aggregateFieldProcessor.getMergeResult(new ArrayList<>(values));
    }

    @Override
    public TumblingTimeTable<IN, ACC, OUT> merge(TumblingTimeTable<IN, ACC, OUT> thatTable) {
        TreeMap<Long, ACC> thatTreeMap = thatTable.getTreeMap();
        thatTreeMap.forEach((tempLong, thatAcc) -> {
            ACC thisAcc = treeMap.get(tempLong);
            if (thisAcc == null) {
                treeMap.put(tempLong, thatAcc);
            } else {
                treeMap.put(tempLong, aggregateFieldProcessor.mergeAccList(CollUtil.toList(thatAcc, thisAcc)));
            }
        });

        TumblingTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTable<>();
        tumblingTimeTable.setTimestamp(Math.max(super.timestamp, thatTable.getTimestamp()));
        tumblingTimeTable.setTreeMap(new TreeMap<>(treeMap));
        return tumblingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(treeMap);
    }

}
