package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 映射型派生指标单元测试类
 */
public class DeriveMetricCalculateMapTest extends DeriveMetricCalculateBase {

    /**
     * 测试基本映射BASEMAP
     * 所有交易账号的累计交易金额
     */
    @Test
    public void testBaseMap() {
        DeriveMetricCalculate<Pair<MultiFieldDistinctKey, Integer>, Map<MultiFieldDistinctKey, Double>,
                Map<MultiFieldDistinctKey, Double>> deriveMetricCalculate
                = metricCalculate.getDeriveMetricCalculate(8L);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("credit_amt_in", "100");
        input1.set("trans_date", "20220609");
        input1.set("debit_amt_out", 800);

        DeriveMetricCalculateResult<Map<MultiFieldDistinctKey, Double>> query =
                deriveMetricCalculate.stateExec(input1);
        Map<MultiFieldDistinctKey, Double> map = new HashMap<>();
        MultiFieldDistinctKey key = new MultiFieldDistinctKey(Collections.singletonList("000000000012"));
        map.put(key, 800.0D);
        assertEquals(map, query.getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("credit_amt_in", "100");
        input2.set("trans_date", "20220609");
        input2.set("debit_amt_out", 900);
        query = deriveMetricCalculate.stateExec(input2);
        map.put(key, 1700.0D);
        assertEquals(map, query.getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("credit_amt_in", "100");
        input3.set("trans_date", "20220609");
        input3.set("debit_amt_out", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        map.put(key, 2700.0D);
        assertEquals(map, query.getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000013");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("credit_amt_in", "100");
        input4.set("trans_date", "20220609");
        input4.set("debit_amt_out", 80);
        query = deriveMetricCalculate.stateExec(input4);

        MultiFieldDistinctKey key2 = new MultiFieldDistinctKey(Collections.singletonList("000000000013"));
        map.put(key2, 80.0D);
        assertEquals(map, query.getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000013");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("credit_amt_in", "100");
        input5.set("trans_date", "20220609");
        input5.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input5);
        map.put(key2, 180.0D);
        assertEquals(map, query.getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("credit_amt_in", "100");
        input6.set("trans_date", "20220609");
        input6.set("debit_amt_out", 120);
        query = deriveMetricCalculate.stateExec(input6);
        map.put(key, 2820.0D);
        assertEquals(map, query.getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000016");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("credit_amt_in", "100");
        input7.set("trans_date", "20220609");
        input7.set("debit_amt_out", 100);
        query = deriveMetricCalculate.stateExec(input7);
        map.put(new MultiFieldDistinctKey(Collections.singletonList("000000000016")), 100.0D);
        assertEquals(map, query.getResult());
    }

}
