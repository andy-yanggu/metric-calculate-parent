package com.yanggu.metric_calculate.core2.table;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

/**
 * 全窗口
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class GlobalTable<IN, ACC, OUT> extends Table<IN, ACC, OUT> {

    private ACC accumulator;

    @Override
    public void put(JSONObject input) {
        accumulator = aggregateFieldProcessor.add(accumulator, getInFromInput(input));
    }

    @Override
    public void query(JSONObject input, DeriveMetricCalculateResult<OUT> deriveMetricCalculateResult) {
        OUT out = aggregateFieldProcessor.getOutFromAcc(accumulator);
        deriveMetricCalculateResult.setResult(out);
    }

}
