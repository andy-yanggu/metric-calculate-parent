package com.yanggu.metric_calculate.core.window;

import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.SESSION_WINDOW;

/**
 * 会话窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@WindowAnnotation(type = SESSION_WINDOW, canMerge = false)
public class SessionWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

    /**
     * 时间字段处理器
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 开始时间戳
     */
    private Long startTimestamp = Long.MIN_VALUE;

    /**
     * 结束时间戳
     */
    private Long endTimestamp = Long.MIN_VALUE + 1;

    /**
     * 会话间隔时间
     */
    private Long gapTimeMillis;

    /**
     * 累计数据
     */
    private List<IN> inList = new ArrayList<>();

    @Override
    public void put(JSONObject input) {
        Long tempTimestamp = timeFieldProcessor.process(input);

        //如果时间差超过间隔时间则生成一个新的会话窗口
        if (tempTimestamp > endTimestamp + gapTimeMillis) {
            inList.clear();
            startTimestamp = tempTimestamp;
        }
        endTimestamp = tempTimestamp + 1;
        //添加度量值
        inList.add(getInFromInput(input));
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        return getOutDeriveMetricCalculateResult(startTimestamp, endTimestamp, inList);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        Long tempTimestamp = timeFieldProcessor.process(input);

        //如果时间差超过间隔时间则生成一个新的会话窗口
        Long tempStartTimestamp = startTimestamp;
        Long tempEndTimestamp = endTimestamp;
        List<IN> tempInList = new ArrayList<>(inList);
        if (tempTimestamp > tempEndTimestamp + gapTimeMillis) {
            tempInList.clear();
            tempStartTimestamp = tempTimestamp;
        }
        tempEndTimestamp = tempTimestamp + 1;
        return getOutDeriveMetricCalculateResult(tempStartTimestamp, tempEndTimestamp, tempInList);
    }

    private DeriveMetricCalculateResult<OUT> getOutDeriveMetricCalculateResult(Long tempStartTimestamp,
                                                                               Long tempEndTimestamp,
                                                                               List<IN> tempInList) {
        OUT outFromInList = aggregateFieldProcessor.getOutFromInList(tempInList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setStartTime(DateUtils.formatDateTime(tempStartTimestamp));
        deriveMetricCalculateResult.setEndTime(DateUtils.formatDateTime(tempEndTimestamp));
        deriveMetricCalculateResult.setResult(outFromInList);
        return deriveMetricCalculateResult;
    }

    //@Override
    public SessionWindow<IN, ACC, OUT> merge(SessionWindow<IN, ACC, OUT> thatWindow) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(inList);
    }

}
