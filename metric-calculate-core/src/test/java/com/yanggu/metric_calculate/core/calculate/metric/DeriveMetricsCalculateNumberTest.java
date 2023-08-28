package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 数值型派生指标单元测试类
 */
class DeriveMetricsCalculateNumberTest extends DeriveMetricsCalculateBase {

    /**
     * 测试SUM求和
     */
    @Test
    void testSum() {
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(1L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("amount", 800);
        input.set("trans_date", "20220609");

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1600.0D, doubles.getResult(), 0.0D);

        input.set("amount", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(2000.0D, doubles.getResult(), 0.0D);
    }

    /**
     * 测试MIN, 最小值
     */
    @Test
    void testMin() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(2L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("amount", 800.0D);
        input.set("trans_date", "20220609");

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.set("amount", 900.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.set("amount", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(400.0D, doubles.getResult(), 0.0D);

        input.set("amount", 500);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(400.0D, doubles.getResult(), 0.0D);
    }

    /**
     * 测试包含前置过滤条件的SUM
     */
    @Test
    void testFilterSum() {
        DeriveMetricCalculate<Double, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(14L);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("amount", 20.0D);
        input.set("trans_date", "20220609");

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertNull(doubles);

        input.set("amount", 800.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.set("amount", 60.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.set("amount", 400.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1200.0D, doubles.getResult(), 0.0D);

        input.set("amount", 20.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1200.0D, doubles.getResult(), 0.0D);
    }

}
