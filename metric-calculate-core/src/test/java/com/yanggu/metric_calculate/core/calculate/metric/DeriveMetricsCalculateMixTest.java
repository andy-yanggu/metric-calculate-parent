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
 * 混合型派生指标单元测试类
 */
@DisplayName("混合型派生指标单元测试类")
class DeriveMetricsCalculateMixTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, BigDecimal> deriveMetricCalculate;

    private static DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, BigDecimal> deriveMetricCalculate2;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(9L);
        deriveMetricCalculate2 = metricCalculate.getDeriveMetricCalculateById(15L);
    }

    /**
     * 测试混合类型BASEMIX
     * 给张三转账的金额占给所有交易对象转账金额的比例
     */
    @ParameterizedTest
    @DisplayName("测试混合类型BASEMIX")
    @CsvSource({"'000000000012',800.0,0.0", "'张三',800.0,0.5", "'张三',800,0.6666666666666666"})
    void testBaseMix(String accountNoIn, BigDecimal amount, BigDecimal expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", accountNoIn);
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<BigDecimal> query = deriveMetricCalculate.stateExec(inputParam);
        assertEquals(expected.doubleValue(), query.getResult().doubleValue(), 0.0001D);
    }

    /**
     * 转出账号近一个月的转出金额，不包含当天
     */
    @ParameterizedTest
    @DisplayName("转出账号近一个月的转出金额，不包含当天")
    @CsvSource({"1696156871000,800,0", "1697550002392,1200,800", "1697636402000,200,2000", "1697636402000,200,2000"})
    void test2(Long tranTimestamp, BigDecimal amount, BigDecimal expected) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account_no_out", "000000000011");
        paramMap.put("account_no_in", "000000000012");
        paramMap.put("trans_timestamp", tranTimestamp);
        paramMap.put("amount", amount);

        paramMap = metricCalculate.getParam(paramMap);
        DeriveMetricCalculateResult<BigDecimal> query = deriveMetricCalculate2.stateExec(paramMap);
        assertNotNull(query);
        assertTrue(NumberUtil.equals(expected, query.getResult()));
    }

}
