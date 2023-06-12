package com.yanggu.metric_calculate.core2.window;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

import static com.yanggu.metric_calculate.core2.enums.WindowTypeEnum.GLOBAL_WINDOW;

/**
 * 全窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
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
        return query(null);
    }

    @Override
    public DeriveMetricCalculateResult<OUT> query(JSONObject input) {
        OUT out = aggregateFieldProcessor.getOutFromAcc(accumulator);
        DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult = new DeriveMetricCalculateResult<>();
        deriveMetricCalculateResult.setResult(out);
        return deriveMetricCalculateResult;
    }

    @Override
    public void deleteData(JSONObject input) {
    }

    @Override
    public void deleteData() {
    }

    @Override
    public boolean isEmpty() {
        return accumulator == null;
    }

    //@Override
    public GlobalWindow<IN, ACC, OUT> merge(GlobalWindow<IN, ACC, OUT> thatGlobalWindow) {
        GlobalWindow<IN, ACC, OUT> globalWindow = new GlobalWindow<>();
        ACC acc = aggregateFieldProcessor.mergeAccList(CollUtil.toList(accumulator, thatGlobalWindow.getAccumulator()));
        globalWindow.setAccumulator(acc);
        return globalWindow;
    }

}
