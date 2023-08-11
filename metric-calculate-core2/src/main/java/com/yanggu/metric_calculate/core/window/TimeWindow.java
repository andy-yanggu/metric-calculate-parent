package com.yanggu.metric_calculate.core.window;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindowData;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 时间窗口
 * <p>滑动时间窗口和滚动时间窗口</p>
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper=false)
public abstract class TimeWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

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

    public DeriveMetricCalculateResult<OUT> query(Long timestamp) {
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        TimeWindowData timeWindowData = timeWindowDataList.get(0);
        OUT query = query(timeWindowData.getWindowStart(), true, timeWindowData.getWindowEnd(), false);
        if (query == null) {
            return null;
        }
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(query);
        deriveMetricCalculateResult.setStartTime(DateUtils.formatDateTime(timeWindowData.getWindowStart()));
        deriveMetricCalculateResult.setEndTime(DateUtils.formatDateTime(timeWindowData.getWindowEnd()));
        return deriveMetricCalculateResult;
    }

}
