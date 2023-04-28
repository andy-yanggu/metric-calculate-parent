package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 对象型派生指标单元测试类
 */
public class DeriveMetricCalculateObjectTest extends DeriveMetricCalculateBase {

    /**
     * 测试对象型MAXFIELD
     * <p>最大交易的金额的交易时间戳</p>
     */
    @Test
    public void testMaxField() {
        DeriveMetricCalculate<KeyValue<MultiFieldOrderCompareKey, String>, MutableObj<KeyValue<MultiFieldOrderCompareKey, String>>, KeyValue<MultiFieldOrderCompareKey, String>> deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(4L);
        DeriveMetricCalculateResult<KeyValue<MultiFieldOrderCompareKey, String>> query;

        JSONObject input1 = new JSONObject();
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("debit_amt_out", 800);

        query = deriveMetricCalculate.stateExec(input1);
        assertEquals("1654768045000", query.getResult().getValue());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768046000");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals("1654768046000", query.getResult().getValue());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("debit_amt_out", 800);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals("1654768046000", query.getResult().getValue());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1354768045000");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals("1354768045000", query.getResult().getValue());
    }

}
