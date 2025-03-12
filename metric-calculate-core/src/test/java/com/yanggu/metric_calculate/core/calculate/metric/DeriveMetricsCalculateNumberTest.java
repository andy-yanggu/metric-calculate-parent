package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.math.NumberUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数值型派生指标单元测试类
 */
@DisplayName("数值型派生指标单元测试类")
class DeriveMetricsCalculateNumberTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Double, BigDecimal, BigDecimal> sumDeriveMetricCalculate;

    private static DeriveMetricCalculate<Double, BigDecimal, BigDecimal> minDeriveMetricCalculate;

    private static DeriveMetricCalculate<Double, BigDecimal, BigDecimal> fitlerSumDeriveMetricCalculate;

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
    void testSum(BigDecimal amount, BigDecimal expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<BigDecimal> doubles = sumDeriveMetricCalculate.stateExec(inputParam);
        assertTrue(NumberUtil.valueEquals(expected, doubles.getResult()));
    }

    /**
     * 测试MIN, 最小值
     */
    @ParameterizedTest
    @DisplayName("测试MIN, 最小值")
    @CsvSource({"800,800.0", "900,800.0", "400,400.0", "500,400.0"})
    void testMin(BigDecimal amount, BigDecimal expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<BigDecimal> doubles = minDeriveMetricCalculate.stateExec(inputParam);
        assertTrue(NumberUtil.valueEquals(expected, doubles.getResult()));
    }

    /**
     * 测试包含前置过滤条件的SUM（amount > 100）
     */
    @ParameterizedTest
    @DisplayName("测试包含前置过滤条件的SUM（amount > 100）")
    @CsvSource(value = {"20,null", "800,800.0", "60,800.0", "400,1200.0", "20,1200.0"}, nullValues = "null")
    void testFilterSum(BigDecimal amount, BigDecimal expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", "000000000012");
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<BigDecimal> result = fitlerSumDeriveMetricCalculate.stateExec(inputParam);
        if (expected == null) {
            assertNull(result);
        } else {
            assertTrue(NumberUtil.valueEquals(expected, result.getResult()));
        }
    }

}
