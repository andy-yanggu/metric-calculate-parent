package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.window.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.window.TimeWindowData;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 时间窗口
 * <p>滑动时间窗口和滚动时间窗口</p>
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class TimeWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

    protected TimeFieldProcessor timeFieldProcessor;

    protected TimeBaselineDimension timeBaselineDimension;

    protected Long timestamp;

    @Override
    public void put(Map<String, Object> input) {
        Long tempTimestamp = timeFieldProcessor.process(input);
        put(tempTimestamp, getInFromInput(input));
    }

    public abstract void put(Long timestamp, IN in);

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        return query(timestamp);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(Map<String, Object> input) {
        Long process = timeFieldProcessor.process(input);
        return query(process);
    }

    /**
     * 查询数据
     *
     * @param timeWindowStart
     * @param timeWindowEnd
     * @return
     */
    public abstract OUT query(Long timeWindowStart, Long timeWindowEnd);

    public DeriveMetricCalculateResult<OUT> query(Long timestamp) {
        List<TimeWindowData> timeWindowDataList = timeBaselineDimension.getTimeWindowList(timestamp);
        TimeWindowData timeWindowData = timeWindowDataList.getFirst();
        long windowStart = timeWindowData.windowStart();
        long windowEnd = timeWindowData.windowEnd();
        OUT query = query(windowStart, windowEnd);
        if (query == null) {
            return null;
        }
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(query);
        deriveMetricCalculateResult.setStartTime(DateUtils.formatDateTime(windowStart));
        deriveMetricCalculateResult.setEndTime(DateUtils.formatDateTime(windowEnd));
        return deriveMetricCalculateResult;
    }

}
