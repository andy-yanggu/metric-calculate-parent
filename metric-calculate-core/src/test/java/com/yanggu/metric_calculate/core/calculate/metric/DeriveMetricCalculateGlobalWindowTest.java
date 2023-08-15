package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 全窗口派生指标单元测试类
 */
class DeriveMetricCalculateGlobalWindowTest extends DeriveMetricCalculateBase {

    /**
     * 占位，首次交易时间
     */
    @Test
    void testOccupiedField() throws Exception {
        DeriveMetricCalculate<Long, MutableObj<Long>, Long> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(12L);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", 1654768045000L);
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        DeriveMetricCalculateResult<Long> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(1654768045000L, query.getResult().longValue());

        JSONObject input2 = input1.clone();
        input2.set("trans_timestamp", 1654768045001L);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(1654768045000L, query.getResult().longValue());

        JSONObject input3 = input1.clone();
        input3.set("trans_timestamp", 1654768045002L);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(1654768045000L, query.getResult().longValue());
    }

}
