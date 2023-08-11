package com.yanggu.metric_calculate.core.window;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.yanggu.metric_calculate.core.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindowData;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper=false)
public class SlidingTimeWindow<IN, ACC, OUT> extends TimeWindow<IN, ACC, OUT> {

    private Map<Pair<Long, Long>, ACC> map = new HashMap<>();

    @Override
    public WindowTypeEnum type() {
        return SLIDING_TIME_WINDOW;
    }

    @Override
    public void put(Long timestamp, IN in) {
        List<TimeWindowData> timeWindowData = timeBaselineDimension.getTimeWindowList(timestamp);
        if (CollUtil.isEmpty(timeWindowData)) {
            return;
        }
        for (TimeWindowData tempTimeWindowData : timeWindowData) {
            long windowStart = tempTimeWindowData.getWindowStart();
            long windowEnd = tempTimeWindowData.getWindowEnd();
            Pair<Long, Long> pair = Pair.of(windowStart, windowEnd);
            ACC historyAcc = map.get(pair);
            ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
            map.put(pair, nowAcc);
        }
        super.timestamp = timestamp;
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return aggregateFieldProcessor.getOutFromAcc(map.get(Pair.of(from, to)));
    }

    @Override
    public void deleteData() {
        Long expireTimestamp = timeBaselineDimension.getExpireTimestamp(timestamp);
        Iterator<Map.Entry<Pair<Long, Long>, ACC>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next().getKey().getKey();
            if (key < expireTimestamp) {
                iterator.remove();
            }
        }
    }

    //@Override
    public SlidingTimeWindow<IN, ACC, OUT> merge(SlidingTimeWindow<IN, ACC, OUT> thatTable) {
        Map<Pair<Long, Long>, ACC> thatMap = thatTable.getMap();
        thatMap.forEach((tempPair, thatAcc) -> {
            ACC thisAcc = map.get(tempPair);
            if (thisAcc == null) {
                map.put(tempPair, thatAcc);
            } else {
                map.put(tempPair, aggregateFieldProcessor.mergeAccList(CollUtil.toList(thisAcc, thatAcc)));
            }
        });

        SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = new SlidingTimeWindow<>();
        slidingTimeTable.setTimestamp(Math.max(super.timestamp, thatTable.getTimestamp()));
        slidingTimeTable.setMap(new HashMap<>(map));
        return slidingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(map);
    }

}
