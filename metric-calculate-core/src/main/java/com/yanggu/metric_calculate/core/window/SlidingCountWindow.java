package com.yanggu.metric_calculate.core.window;

import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.SLIDING_COUNT_WINDOW;

/**
 * 滑动计数窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@WindowAnnotation(type = SLIDING_COUNT_WINDOW, canMerge = false)
public class SlidingCountWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

    private Integer limit;

    private List<IN> inList = new ArrayList<>();

    @Override
    public void put(JSONObject input) {
        IN in = aggregateFieldProcessor.process(input);
        inList.add(in);
        while (inList.size() > limit) {
            inList.remove(0);
        }
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        OUT out = aggregateFieldProcessor.getOutFromInList(inList);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(out);
        return deriveMetricCalculateResult;
    }

    //@Override
    public SlidingCountWindow<IN, ACC, OUT> merge(SlidingCountWindow<IN, ACC, OUT> thatWindow) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return CollUtil.isEmpty(inList);
    }

}
