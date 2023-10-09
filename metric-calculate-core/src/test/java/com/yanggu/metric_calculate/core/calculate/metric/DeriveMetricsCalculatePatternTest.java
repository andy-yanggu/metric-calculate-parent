package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * CEP型派生指标单元测试类
 */
class DeriveMetricsCalculatePatternTest extends DeriveMetricsCalculateBase {

    @Test
    void testCEP() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(10L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("amount", 800);
        DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(input);
        System.out.println(deriveMetricCalculateResults);
    }

}
