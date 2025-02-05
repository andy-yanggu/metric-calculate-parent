package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * CEP型派生指标单元测试类
 */
class DeriveMetricsCalculatePatternTest extends DeriveMetricsCalculateBase {

    @Test
    void testCEP() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(10L);

        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", "000000000011");
        input.put("account_no_in", "000000000012");
        input.put("trans_timestamp", "1654768045000");
        input.put("amount", 800);
        DeriveMetricCalculateResult<Double> deriveMetricCalculateResults = deriveMetricCalculate.stateExec(input);
        System.out.println(deriveMetricCalculateResults);
    }

}
