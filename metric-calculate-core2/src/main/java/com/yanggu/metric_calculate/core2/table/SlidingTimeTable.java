package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlidingTimeTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    private Map<Pair<Long, Long>, ACC> map = new HashMap<>();

    @Override
    public void put(Long timestamp, IN in) {
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(timestamp);
        if (CollUtil.isEmpty(timeWindow)) {
            return;
        }
        for (TimeWindow tempTimeWindow : timeWindow) {
            long windowStart = tempTimeWindow.getWindowStart();
            long windowEnd = tempTimeWindow.getWindowEnd();
            Pair<Long, Long> tuple2 = Pair.of(windowStart, windowEnd);
            ACC historyAcc = map.get(tuple2);
            ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
            map.put(tuple2, nowAcc);
        }
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return aggregateFieldProcessor.getOut(map.get(Pair.of(from, to)));
    }

}
