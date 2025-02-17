package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 混合型派生指标单元测试类
 */
@DisplayName("混合型派生指标单元测试类")
class DeriveMetricsCalculateMixTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, Double> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(9L);
    }

    /**
     * 测试混合类型BASEMIX
     * 给张三转账的金额占给所有交易对象转账金额的比例
     */
    @ParameterizedTest
    @DisplayName("测试混合类型BASEMIX")
    @CsvSource({"'000000000012',800.0,0.0", "'张三',800.0,0.5", "'张三',800,0.6666666666666666"})
    void testBaseMix(String accountNoIn, Double amount, Double expected) {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("account_no_out", "000000000011");
        inputParam.put("account_no_in", accountNoIn);
        inputParam.put("trans_timestamp", "1654768045000");
        inputParam.put("amount", amount);

        DeriveMetricCalculateResult<Double> query = deriveMetricCalculate.stateExec(inputParam);
        assertEquals(expected, query.getResult(), 0.0D);
    }

    /**
     * 转出账号近一个月的转出金额，不包含当天
     */
    @Test
    void test2() {
        DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(15L);

        DeriveMetricCalculateResult<Double> query;

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        //时间戳为2023-10-01 18:41:11
        input1.put("trans_timestamp", 1696156871000L);
        input1.put("amount", 800);
        input1 = metricCalculate.getParam(input1);
        query = deriveMetricCalculate.stateExec(input1);
        Double result = query.getResult();
        assertEquals(0.0D, result, 0.0D);

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        //时间戳为2023-10-17 21:40:02
        input2.put("trans_timestamp", 1697550002392L);
        input2.put("amount", 1200);
        input2 = metricCalculate.getParam(input2);
        query = deriveMetricCalculate.stateExec(input2);
        result = query.getResult();
        assertEquals(800.0D, result, 0.0D);

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "000000000012");
        //时间戳为2023-10-18 21:40:02
        input3.put("trans_timestamp", 1697636402000L);
        input3.put("amount", 200);
        input3 = metricCalculate.getParam(input3);
        query = deriveMetricCalculate.stateExec(input3);
        result = query.getResult();
        assertEquals(2000.0D, result, 0.0D);
    }

}
