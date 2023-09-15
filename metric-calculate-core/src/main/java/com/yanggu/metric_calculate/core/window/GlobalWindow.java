package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.json.JSONObject;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.GLOBAL_WINDOW;

/**
 * 全窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalWindow<IN, ACC, OUT> extends AbstractWindow<IN, ACC, OUT> {

    private ACC accumulator;

    @Override
    public WindowTypeEnum type() {
        return GLOBAL_WINDOW;
    }

    @Override
    public void put(JSONObject input) {
        accumulator = aggregateFieldProcessor.add(accumulator, getInFromInput(input));
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query() {
        OUT out = aggregateFieldProcessor.getOutFromAcc(accumulator);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(out);
        return deriveMetricCalculateResult;
    }

    @Override
    public boolean isEmpty() {
        return accumulator == null;
    }

    //@Override
    public GlobalWindow<IN, ACC, OUT> merge(GlobalWindow<IN, ACC, OUT> thatGlobalWindow) {
        GlobalWindow<IN, ACC, OUT> globalWindow = new GlobalWindow<>();
        ACC acc = aggregateFieldProcessor.mergeAccList(ListUtil.of(accumulator, thatGlobalWindow.getAccumulator()));
        globalWindow.setAccumulator(acc);
        return globalWindow;
    }

    @Override
    public String toString() {
        return "GlobalWindow{" +
                "accumulator=" + accumulator +
                '}';
    }

}
