package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 映射型派生指标单元测试类
 */
class DeriveMetricsCalculateMapTest extends DeriveMetricsCalculateBase {

    /**
     * 测试基本映射BASEMAP
     * 所有交易账号的累计交易金额
     */
    @Test
    void testBaseMap() {
        DeriveMetricCalculate<AbstractMap.SimpleImmutableEntry<MultiFieldDistinctKey, Integer>, Map<MultiFieldDistinctKey, Double>,
                        Map<List<Object>, Double>> deriveMetricCalculate
                = metricCalculate.getDeriveMetricCalculateById(8L);

        JSONObject input1 = new JSONObject();
        input1.set("account_no_out", "000000000011");
        input1.set("account_no_in", "000000000012");
        input1.set("trans_timestamp", "1654768045000");
        input1.set("amount", 800);

        DeriveMetricCalculateResult<Map<List<Object>, Double>> query =
                deriveMetricCalculate.stateExec(input1);
        Map<List<Object>, Double> map = new HashMap<>();
        List<Object> key = List.of("000000000012");
        map.put(List.of("000000000012"), 800.0D);
        assertEquals(map, query.getResult());

        JSONObject input2 = new JSONObject();
        input2.set("account_no_out", "000000000011");
        input2.set("account_no_in", "000000000012");
        input2.set("trans_timestamp", "1654768045000");
        input2.set("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        map.put(key, 1700.0D);
        assertEquals(map, query.getResult());

        JSONObject input3 = new JSONObject();
        input3.set("account_no_out", "000000000011");
        input3.set("account_no_in", "000000000012");
        input3.set("trans_timestamp", "1654768045000");
        input3.set("amount", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        map.put(key, 2700.0D);
        assertEquals(map, query.getResult());

        JSONObject input4 = new JSONObject();
        input4.set("account_no_out", "000000000011");
        input4.set("account_no_in", "000000000013");
        input4.set("trans_timestamp", "1654768045000");
        input4.set("amount", 80);
        query = deriveMetricCalculate.stateExec(input4);

        List<Object> key2 = List.of("000000000013");
        map.put(key2, 80.0D);
        assertEquals(map, query.getResult());

        JSONObject input5 = new JSONObject();
        input5.set("account_no_out", "000000000011");
        input5.set("account_no_in", "000000000013");
        input5.set("trans_timestamp", "1654768045000");
        input5.set("amount", 100);
        query = deriveMetricCalculate.stateExec(input5);
        map.put(key2, 180.0D);
        assertEquals(map, query.getResult());

        JSONObject input6 = new JSONObject();
        input6.set("account_no_out", "000000000011");
        input6.set("account_no_in", "000000000012");
        input6.set("trans_timestamp", "1654768045000");
        input6.set("amount", 120);
        query = deriveMetricCalculate.stateExec(input6);
        map.put(key, 2820.0D);
        assertEquals(map, query.getResult());

        JSONObject input7 = new JSONObject();
        input7.set("account_no_out", "000000000011");
        input7.set("account_no_in", "000000000016");
        input7.set("trans_timestamp", "1654768045000");
        input7.set("amount", 100);
        query = deriveMetricCalculate.stateExec(input7);
        map.put(List.of("000000000016"), 100.0D);
        assertEquals(map, query.getResult());
    }

}
