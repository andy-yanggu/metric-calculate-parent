package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import lombok.Data;

import java.util.List;

/**
 * 时间窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public abstract class TimeTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    protected TimeFieldProcessor timeFieldProcessor;

    protected TimeBaselineDimension timeBaselineDimension;

    protected Long timestamp;

    @Override
    public void put(JSONObject input) {
        Long tempTimestamp = timeFieldProcessor.process(input);
        put(tempTimestamp, getInFromInput(input));
    }

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


    public abstract void put(Long timestamp, IN in);

}
