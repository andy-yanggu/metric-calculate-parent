package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 滑动计数求和派生指标单元测试类
 */
public class DeriveMetricCalculateSlidingCountWindowTest extends DeriveMetricCalculateBase {

    /**
     * 测试滑动计数窗口limit限制为5, 最近5笔交易, 进行求和
     */
    @Test
    public void testSum() throws Exception {
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(11L);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        DeriveMetricCalculateResult<Double> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(800.0D, query.getResult(), 0.0D);

        JSONObject input2 = input1.clone();
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(1700.0D, query.getResult(), 0.0D);

        JSONObject input3 = input1.clone();
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(2700.0D, query.getResult(), 0.0D);

        JSONObject input4 = input1.clone();
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(3800.0D, query.getResult(), 0.0D);

        JSONObject input5 = input1.clone();
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(3900.0D, query.getResult(), 0.0D);

        JSONObject input6 = input1.clone();
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(3200.0D, query.getResult(), 0.0D);

        JSONObject input7 = input1.clone();
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(2400.0D, query.getResult(), 0.0D);
    }

}
