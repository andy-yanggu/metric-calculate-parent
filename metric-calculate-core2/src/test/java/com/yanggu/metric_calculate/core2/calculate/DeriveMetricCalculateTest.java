package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeriveMetricCalculateTest {

    private MetricCalculate metricCalculate;

    @Before
    public void init() {
        String jsonString = FileUtil.readUtf8String("test3.json");
        MetricCalculate tempMetricCalculate = JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate>() {}, true);
        MetricUtil.getFieldMap(tempMetricCalculate);
        this.metricCalculate = tempMetricCalculate;
    }

    @Test
    public void testExec1() {
        Derive derive = this.metricCalculate.getDerive().get(0);
        DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate = MetricUtil.initDerive(derive, metricCalculate);
        DeriveMetricMiddleHashMapStore deriveMetricMiddleHashMapStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleHashMapStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapStore);

        JSONObject input = new JSONObject();
        input.set("account_no_out", "000000000011");
        input.set("account_no_in", "000000000012");
        input.set("trans_timestamp", "1654768045000");
        input.set("credit_amt_in", 100);
        input.set("debit_amt_out", 800);
        input.set("trans_date", "20220609");

        List<Double> doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(800.0D, doubles.get(0), 0.0D);

        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(1600.0D, doubles.get(0), 0.0D);

        input.set("debit_amt_out", 400);
        doubles = deriveMetricCalculate.stateExec(input);
        assertEquals(2000.0D, doubles.get(0), 0.0D);
    }

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    public void test5() {
        Derive derive = this.metricCalculate.getDerive().get(4);
        DeriveMetricCalculate<JSONObject, List<JSONObject>, List<JSONObject>> deriveMetricCalculate =
                MetricUtil.initDerive(derive, metricCalculate);
        DeriveMetricMiddleHashMapStore deriveMetricMiddleHashMapStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleHashMapStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleHashMapStore);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", "800");

        List<List<JSONObject>> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(Collections.singletonList(input1), query.get(0));

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(Arrays.asList(input1, input2), query.get(0));

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(Arrays.asList(input1, input2, input3), query.get(0));

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000012");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(Arrays.asList(input1, input2, input3, input4), query.get(0));

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000012");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0));

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0));

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000012");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(Arrays.asList(input1, input2, input3, input4, input5), query.get(0));
    }

}
