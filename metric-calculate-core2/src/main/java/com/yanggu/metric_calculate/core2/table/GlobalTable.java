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
public class GlobalTable<IN, ACC, OUT> extends AbstractTable<IN, ACC, OUT> {

    private ACC accumulator;

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
    public boolean isEmpty() {
        return accumulator == null;
    }

}
