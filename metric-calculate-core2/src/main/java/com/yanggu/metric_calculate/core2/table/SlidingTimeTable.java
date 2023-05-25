package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 滑动时间窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class SlidingTimeTable<IN, ACC, OUT> extends TimeTable<IN, ACC, OUT, SlidingTimeTable<IN, ACC, OUT>> {

    private Map<Pair<Long, Long>, ACC> map = new HashMap<>();

    @Override
    public void put(Long timestamp, IN in) {
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindowList(timestamp);
        if (CollUtil.isEmpty(timeWindow)) {
            return;
        }
        for (TimeWindow tempTimeWindow : timeWindow) {
            long windowStart = tempTimeWindow.getWindowStart();
            long windowEnd = tempTimeWindow.getWindowEnd();
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
    public SlidingTimeTable<IN, ACC, OUT> merge(SlidingTimeTable<IN, ACC, OUT> thatTable) {
        Map<Pair<Long, Long>, ACC> thatMap = thatTable.getMap();
        thatMap.forEach((tempPair, thatAcc) -> {
            ACC thisAcc = map.get(tempPair);
            if (thisAcc == null) {
                map.put(tempPair, thatAcc);
            } else {
                map.put(tempPair, aggregateFieldProcessor.mergeAccList(CollUtil.toList(thisAcc, thatAcc)));
            }
        });

        SlidingTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTable<>();
        slidingTimeTable.setTimestamp(Math.max(super.timestamp, thatTable.getTimestamp()));
        slidingTimeTable.setMap(new HashMap<>(map));
        return slidingTimeTable;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(map);
    }

}
