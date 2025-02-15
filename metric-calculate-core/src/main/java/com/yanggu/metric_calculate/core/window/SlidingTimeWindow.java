package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.pojo.window.TimeWindowData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.SLIDING_TIME_WINDOW;

/**
 * 滑动时间窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@WindowAnnotation(type = SLIDING_TIME_WINDOW, canMerge = true)
public class SlidingTimeWindow<IN, ACC, OUT> extends TimeWindow<IN, ACC, OUT> {

    private Map<Pair<Long, Long>, ACC> map = new HashMap<>();

    @Override
    public void put(Long timestamp, IN in) {
        List<TimeWindowData> timeWindowData = timeBaselineDimension.getTimeWindowList(timestamp);
        if (CollUtil.isEmpty(timeWindowData)) {
            return;
        }
        for (TimeWindowData tempTimeWindowData : timeWindowData) {
            long windowStart = tempTimeWindowData.windowStart();
            long windowEnd = tempTimeWindowData.windowEnd();
            Pair<Long, Long> pair = Pair.of(windowStart, windowEnd);
            ACC historyAcc = map.get(pair);
            ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
            map.put(pair, nowAcc);
        }
        super.timestamp = timestamp;
    }

    @Override
    public OUT query(Long timeWindowStart, Long timeWindowEnd) {
        return aggregateFieldProcessor.getOutFromAcc(map.get(Pair.of(timeWindowStart, timeWindowEnd)));
    }

    @Override
    public void deleteData() {
        Long expireTimestamp = timeBaselineDimension.getExpireTimestamp(timestamp);
        Iterator<Map.Entry<Pair<Long, Long>, ACC>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next().getKey().getLeft();
            if (key < expireTimestamp) {
                iterator.remove();
            }
        }
    }

    //@Override
    public SlidingTimeWindow<IN, ACC, OUT> merge(SlidingTimeWindow<IN, ACC, OUT> thatWindow) {
        Map<Pair<Long, Long>, ACC> thisMap = new HashMap<>(map);
        Map<Pair<Long, Long>, ACC> thatMap = thatWindow.getMap();
        thatMap.forEach((tempPair, thatAcc) -> {
            ACC thisAcc = thisMap.get(tempPair);
            if (thisAcc == null) {
                thisMap.put(tempPair, thatAcc);
            } else {
                thisMap.put(tempPair, aggregateFieldProcessor.mergeAccList(List.of(thisAcc, thatAcc)));
            }
        });

        SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = new SlidingTimeWindow<>();
        slidingTimeTable.setTimestamp(Math.max(super.timestamp, thatWindow.getTimestamp()));
        slidingTimeTable.setMap(thisMap);
        return slidingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return MapUtil.isEmpty(map);
    }

}
