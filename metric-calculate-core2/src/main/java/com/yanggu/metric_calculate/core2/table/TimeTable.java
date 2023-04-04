package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import lombok.Setter;

import java.util.List;

/**
 * 进行分桶的抽象类
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
public abstract class TimeTable<IN, ACC, OUT> implements Table<IN, ACC, OUT> {

    @Setter
    protected AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    @Setter
    protected TimeBaselineDimension timeBaselineDimension;

    protected Long timestamp;

    @Override
    public OUT query() {
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        TimeWindow timeWindow = timeWindowList.get(0);
        return query(timeWindow.getWindowStart(), true, timeWindow.getWindowEnd(), false);
    }

    /**
     * 查询数据
     *
     * @param from
     * @param fromInclusive
     * @param to
     * @param toInclusive
     * @return
     */
    public abstract OUT query(Long from, boolean fromInclusive, Long to, boolean toInclusive);

}
