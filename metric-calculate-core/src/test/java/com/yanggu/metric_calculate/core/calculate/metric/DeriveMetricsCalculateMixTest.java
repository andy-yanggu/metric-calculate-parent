package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 混合型派生指标单元测试类
 */
class DeriveMetricsCalculateMixTest extends DeriveMetricsCalculateBase {

    /**
     * 测试混合类型BASEMIX
     */
    @Test
    void testBaseMix() throws Exception {
        DeriveMetricCalculate<Map<String, Object>, Map<String, Object>, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(9L);

        DeriveMetricCalculateResult<Double> query;

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", 800);

        query = deriveMetricCalculate.stateExec(input1);

        Double result = query.getResult();
        //0 / 800
        assertEquals(0.0D, result, 0.0D);

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "张三");
        input2.put("trans_timestamp", "1654768045000");
        input2.put("amount", 800);
        query = deriveMetricCalculate.stateExec(input2);
        result = query.getResult();
        //800 / 1600
        assertEquals(0.5D, result, 0.0D);

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "张三");
        input3.put("trans_timestamp", "1654768045000");
        input3.put("amount", 800);
        query = deriveMetricCalculate.stateExec(input3);
        result = query.getResult();
        //1600 / 2400
        assertEquals(new BigDecimal("0.66666").doubleValue(), result, 0.001D);
    }

    /**
     * 转出账号近一个月的转出金额，不包含当天
     *
     * @throws Exception
     */
    @Test
    void test2() throws Exception {
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
