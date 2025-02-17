package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 数值型派生指标单元测试类
 */
@DisplayName("数值型派生指标单元测试类")
class DeriveMetricsCalculateNumberTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Double, Double, Double> sumDeriveMetricCalculate;

    private static DeriveMetricCalculate<Double, Double, Double> minDeriveMetricCalculate;

    private static DeriveMetricCalculate<Double, Double, Double> fitlerSumDeriveMetricCalculate;

    @BeforeAll
    static void init() {
        sumDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(1L);
        minDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(2L);
        fitlerSumDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(14L);
    }

    /**
     * 测试SUM求和
     */
    @ParameterizedTest
    @DisplayName("测试SUM求和")
    @CsvSource({"800,800.0", "800,1600.0", "400,2000.0"})
    void testSum(Double amount, Double expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<Double> doubles = sumDeriveMetricCalculate.stateExec(inputParam);
        assertEquals(expected, doubles.getResult(), 0.0D);
    }

    /**
     * 测试MIN, 最小值
     */
    @ParameterizedTest
    @DisplayName("测试MIN, 最小值")
    @CsvSource({"800,800.0", "900,800.0", "400,400.0", "500,400.0"})
    void testMin(Double amount, Double expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<Double> doubles = minDeriveMetricCalculate.stateExec(inputParam);
        assertEquals(expected, doubles.getResult(), 0.0D);
    }

    /**
     * 测试包含前置过滤条件的SUM（amount > 100）
     */
    @ParameterizedTest
    @DisplayName("测试包含前置过滤条件的SUM（amount > 100）")
    @CsvSource(value = {"20,null", "800,800.0", "60,800.0", "400,1200.0", "20,1200.0"}, nullValues = "null")
    void testFilterSum(Double amount, Double expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<Double> result = fitlerSumDeriveMetricCalculate.stateExec(inputParam);
        if (expected == null) {
            assertNull(result);
        } else {
            assertEquals(expected, result.getResult(), 0.0D);
        }
    }

}
