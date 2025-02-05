package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 滑动计数求和派生指标单元测试类
 */
class DeriveMetricsCalculateSlidingCountWindowTest extends DeriveMetricsCalculateBase {

    /**
     * 测试滑动计数窗口limit限制为5, 最近5笔交易, 进行求和
     */
    @Test
    void testSum() throws Exception {
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(11L);

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", 800);

        DeriveMetricCalculateResult<Double> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(800.0D, query.getResult(), 0.0D);

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", "1654768045000");
        input2.put("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(1700.0D, query.getResult(), 0.0D);

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "000000000012");
        input3.put("trans_timestamp", "1654768045000");
        input3.put("amount", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(2700.0D, query.getResult(), 0.0D);

        Map<String, Object> input4 = new HashMap<>();
        input4.put("account_no_out", "000000000011");
        input4.put("account_no_in", "000000000012");
        input4.put("trans_timestamp", "1654768045000");
        input4.put("amount", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(3800.0D, query.getResult(), 0.0D);

        Map<String, Object> input5 = new HashMap<>();
        input5.put("account_no_out", "000000000011");
        input5.put("account_no_in", "000000000012");
        input5.put("trans_timestamp", "1654768045000");
        input5.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(3900.0D, query.getResult(), 0.0D);

        Map<String, Object> input6 = new HashMap<>();
        input6.put("account_no_out", "000000000011");
        input6.put("account_no_in", "000000000012");
        input6.put("trans_timestamp", "1654768045000");
        input6.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(3200.0D, query.getResult(), 0.0D);

        Map<String, Object> input7 = new HashMap<>();
        input7.put("account_no_out", "000000000011");
        input7.put("account_no_in", "000000000012");
        input7.put("trans_timestamp", "1654768045000");
        input7.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(2400.0D, query.getResult(), 0.0D);
    }

}
