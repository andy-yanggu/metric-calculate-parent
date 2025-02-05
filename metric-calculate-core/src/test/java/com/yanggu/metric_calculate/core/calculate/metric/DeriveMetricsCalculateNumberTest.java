package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", "000000000011");
        input.put("account_no_in", "000000000012");
        input.put("trans_timestamp", "1654768045000");
        input.put("amount", 800);

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1600.0D, doubles.getResult(), 0.0D);

        input.put("amount", 400);
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

        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", "000000000011");
        input.put("account_no_in", "000000000012");
        input.put("trans_timestamp", "1654768045000");
        input.put("amount", 800.0D);

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.put("amount", 900.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.put("amount", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(400.0D, doubles.getResult(), 0.0D);

        input.put("amount", 500);
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

        Map<String, Object> input = new HashMap<>();
        input.put("account_no_out", "000000000011");
        input.put("account_no_in", "000000000012");
        input.put("trans_timestamp", "1654768045000");
        input.put("amount", 20.0D);

        DeriveMetricCalculateResult<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertNull(doubles);

        input.put("amount", 800.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.put("amount", 60.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.getResult(), 0.0D);

        input.put("amount", 400.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1200.0D, doubles.getResult(), 0.0D);

        input.put("amount", 20.0D);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1200.0D, doubles.getResult(), 0.0D);
    }

}
