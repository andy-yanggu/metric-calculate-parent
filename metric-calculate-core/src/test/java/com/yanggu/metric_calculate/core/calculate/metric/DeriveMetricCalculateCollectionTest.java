package com.yanggu.metric_calculate.core.calculate.metric;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.KeyValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 集合型派生指标单元测试类
 */
class DeriveMetricCalculateCollectionTest extends DeriveMetricCalculateBase {

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    void testListObject() {
        DeriveMetricCalculate<JSONObject, List<JSONObject>, List<JSONObject>> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(6L);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");

        DeriveMetricCalculateResult<List<JSONObject>> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(Collections.singletonList(input1), query.getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(Arrays.asList(input1, input2), query.getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(Arrays.asList(input1, input2, input3), query.getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(Arrays.asList(input1, input2, input3, input4), query.getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.getResult());
    }

    /**
     * 测试有序列表对象, 按照debit_amt_out升序排序, 取5个
     * <p>SORTEDLIMITLISTOBJECT</p>
     */
    @Test
    void testSort_list_object() throws Exception {
        DeriveMetricCalculate<KeyValue<MultiFieldOrderCompareKey, JSONObject>, BoundedPriorityQueue<KeyValue<MultiFieldOrderCompareKey, JSONObject>>, List<KeyValue<MultiFieldOrderCompareKey, JSONObject>>> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(7L);
        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        List<KeyValue<MultiFieldOrderCompareKey, JSONObject>> stateExec = deriveMetricCalculate.stateExec(input1).getResult();
        assertEquals(1, stateExec.size());
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setDataList(CollUtil.toList(800));
        KeyValue<MultiFieldOrderCompareKey, JSONObject> expected = new KeyValue<>(multiFieldOrderCompareKey, input1);
        KeyValue<MultiFieldOrderCompareKey, JSONObject> actual = stateExec.get(0);
        assertEquals(expected, actual);

        JSONObject input2 = input1.clone();
        input2.set("debit_amt_out", 900);
        stateExec = deriveMetricCalculate.stateExec(input2).getResult();
        assertEquals(2, stateExec.size());
        assertEquals(new KeyValue<>(new MultiFieldOrderCompareKey(CollUtil.toList(800)), input2), stateExec.get(0));
        assertEquals(new KeyValue<>(new MultiFieldOrderCompareKey(CollUtil.toList(900)), input1), stateExec.get(1));
    }

}
