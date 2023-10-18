package com.yanggu.metric_calculate.core.calculate.metric;


import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 对象型派生指标单元测试类
 */
class DeriveMetricsCalculateObjectTest extends DeriveMetricsCalculateBase {

    /**
     * 测试对象型MAXFIELD
     * <p>最大交易的金额的交易时间戳</p>
     */
    @Test
    void testMaxField() {
        DeriveMetricCalculate<Pair<MultiFieldData, String>, MutableObj<Pair<MultiFieldData, String>>, String> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(4L);
        DeriveMetricCalculateResult<String> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("amount", 800);

        query = deriveMetricCalculate.stateExec(input1);
        assertEquals("1654768045000", query.getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768046000");
        input2.set("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals("1654768046000", query.getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("amount", 800);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals("1654768046000", query.getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1354768045000");
        input4.set("amount", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals("1354768045000", query.getResult());
    }

}
