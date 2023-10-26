package com.yanggu.metric_calculate.core.window;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;

import java.util.*;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.TUMBLING_TIME_WINDOW;

/**
 * 滚动时间窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@WindowAnnotation(type = TUMBLING_TIME_WINDOW, canMerge = true)
public class TumblingTimeWindow<IN, ACC, OUT> extends TimeWindow<IN, ACC, OUT> {

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
    public OUT query(Long timeWindowStart, Long timeWindowEnd) {
        ACC acc = treeMap.get(timeWindowStart);
        return aggregateFieldProcessor.getOutFromAcc(acc);
    }

    @Override
    public void deleteData() {
        Long expireTimestamp = timeBaselineDimension.getExpireTimestamp(timestamp);
        Iterator<Map.Entry<Long, ACC>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next().getKey();
            if (key < expireTimestamp) {
                iterator.remove();
            }
        }
    }

    //@Override
    public TumblingTimeWindow<IN, ACC, OUT> merge(TumblingTimeWindow<IN, ACC, OUT> tumblingTimeWindow) {
        TreeMap<Long, ACC> thatTreeMap = tumblingTimeWindow.getTreeMap();
        thatTreeMap.forEach((tempLong, thatAcc) -> {
            ACC thisAcc = treeMap.get(tempLong);
            if (thisAcc == null) {
                treeMap.put(tempLong, thatAcc);
            } else {
                treeMap.put(tempLong, aggregateFieldProcessor.mergeAccList(ListUtil.of(thatAcc, thisAcc)));
            }
        });

        TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeWindow<>();
        tumblingTimeTable.setTimestamp(Math.max(super.timestamp, tumblingTimeWindow.getTimestamp()));
        tumblingTimeTable.setTreeMap(new TreeMap<>(treeMap));
        return tumblingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(treeMap);
    }

}
