package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core2.util.DateUtils;
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
public abstract class TimeTable<IN, ACC, OUT, T extends Table<IN, ACC, OUT, T>> extends AbstractTable<IN, ACC, OUT, T> {

    protected TimeFieldProcessor timeFieldProcessor;

    protected TimeBaselineDimension timeBaselineDimension;

    protected Long timestamp;

    @Override
    public void put(JSONObject input) {
        Long tempTimestamp = timeFieldProcessor.process(input);
        put(tempTimestamp, getInFromInput(input));
    }

    public abstract void put(Long timestamp, IN in);

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        return query(timestamp);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        Long process = timeFieldProcessor.process(input);
        return query(process);
    }

    public DeriveMetricCalculateResult<OUT> query(Long timestamp) {
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindowList(timestamp);
        TimeWindow timeWindow = timeWindowList.get(0);
        OUT query = query(timeWindow.getWindowStart(), true, timeWindow.getWindowEnd(), false);
        if (query == null) {
            return null;
        }
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(query);
        deriveMetricCalculateResult.setStartTime(DateUtils.formatDateTime(timeWindow.getWindowStart()));
        deriveMetricCalculateResult.setEndTime(DateUtils.formatDateTime(timeWindow.getWindowEnd()));
        return deriveMetricCalculateResult;
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
