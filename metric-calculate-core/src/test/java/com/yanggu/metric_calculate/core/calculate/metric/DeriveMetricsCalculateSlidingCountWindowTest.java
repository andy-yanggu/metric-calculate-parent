package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

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

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("amount", 800);

        DeriveMetricCalculateResult<Double> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(800.0D, query.getResult(), 0.0D);

        JSONObject input2 = input1.clone();
        input2.set("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(1700.0D, query.getResult(), 0.0D);

        JSONObject input3 = input1.clone();
        input3.set("amount", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(2700.0D, query.getResult(), 0.0D);

        JSONObject input4 = input1.clone();
        input4.set("amount", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(3800.0D, query.getResult(), 0.0D);

        JSONObject input5 = input1.clone();
        input5.set("amount", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(3900.0D, query.getResult(), 0.0D);

        JSONObject input6 = input1.clone();
        input6.set("amount", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(3200.0D, query.getResult(), 0.0D);

        JSONObject input7 = input1.clone();
        input7.set("amount", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(2400.0D, query.getResult(), 0.0D);
    }

}
