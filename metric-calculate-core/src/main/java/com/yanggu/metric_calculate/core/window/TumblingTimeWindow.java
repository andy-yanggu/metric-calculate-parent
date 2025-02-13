package com.yanggu.metric_calculate.core.window;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.map.MapUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        TreeMap<Long, ACC> thisTreeMap = new TreeMap<>(treeMap);
        thatTreeMap.forEach((tempLong, thatAcc) -> {
            ACC thisAcc = thisTreeMap.get(tempLong);
            if (thisAcc == null) {
                thisTreeMap.put(tempLong, thatAcc);
            } else {
                thisTreeMap.put(tempLong, aggregateFieldProcessor.mergeAccList(List.of(thatAcc, thisAcc)));
            }
        });

        TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeWindow<>();
        tumblingTimeTable.setTimestamp(Math.max(super.timestamp, tumblingTimeWindow.getTimestamp()));

        tumblingTimeTable.setTreeMap(thisTreeMap);
        return tumblingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return MapUtil.isEmpty(treeMap);
    }

}
