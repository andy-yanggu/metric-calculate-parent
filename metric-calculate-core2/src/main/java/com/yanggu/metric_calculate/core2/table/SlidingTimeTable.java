package com.yanggu.metric_calculate.core2.table;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AbstractAggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlidingTimeTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    private Map<Tuple2<Long, Long>, ACC> map = new HashMap<>();

    public SlidingTimeTable(AbstractAggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor,
                            TimeBaselineDimension timeBaselineDimension) {
        super(aggregateFieldProcessor, timeBaselineDimension);
    }

    @Override
    public void put(Long timestamp, IN in) {
        List<TimeWindow> timeWindow = timeBaselineDimension.getTimeWindow(timestamp);
        if (CollUtil.isEmpty(timeWindow)) {
            return;
        }
        for (TimeWindow tempTimeWindow : timeWindow) {
            long windowStart = tempTimeWindow.getWindowStart();
            long windowEnd = tempTimeWindow.getWindowEnd();
            Tuple2<Long, Long> tuple2 = Tuples.of(windowStart, windowEnd);
            ACC historyAcc = map.get(tuple2);
            ACC nowAcc = aggregateFieldProcessor.add(historyAcc, in);
            map.put(tuple2, nowAcc);
        }
    }

    @Override
    public OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive) {
        return aggregateFieldProcessor.getOut(map.get(Tuples.of(from, to)));
    }

}
