package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
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
        DeriveMetricCalculate<AbstractMap.SimpleImmutableEntry<MultiFieldData, Integer>, Map<MultiFieldData, Double>,
                        Map<List<Object>, Double>> deriveMetricCalculate
                = metricCalculate.getDeriveMetricCalculateById(8L);

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", 800);

        DeriveMetricCalculateResult<Map<List<Object>, Double>> query =
                deriveMetricCalculate.stateExec(input1);
        Map<List<Object>, Double> map = new HashMap<>();
        List<Object> key = List.of("000000000012");
        map.put(List.of("000000000012"), 800.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", "1654768045000");
        input2.put("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        map.put(key, 1700.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "000000000012");
        input3.put("trans_timestamp", "1654768045000");
        input3.put("amount", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        map.put(key, 2700.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input4 = new HashMap<>();
        input4.put("account_no_out", "000000000011");
        input4.put("account_no_in", "000000000013");
        input4.put("trans_timestamp", "1654768045000");
        input4.put("amount", 80);
        query = deriveMetricCalculate.stateExec(input4);

        List<Object> key2 = List.of("000000000013");
        map.put(key2, 80.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input5 = new HashMap<>();
        input5.put("account_no_out", "000000000011");
        input5.put("account_no_in", "000000000013");
        input5.put("trans_timestamp", "1654768045000");
        input5.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input5);
        map.put(key2, 180.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input6 = new HashMap<>();
        input6.put("account_no_out", "000000000011");
        input6.put("account_no_in", "000000000012");
        input6.put("trans_timestamp", "1654768045000");
        input6.put("amount", 120);
        query = deriveMetricCalculate.stateExec(input6);
        map.put(key, 2820.0D);
        assertEquals(map, query.getResult());

        Map<String, Object> input7 = new HashMap<>();
        input7.put("account_no_out", "000000000011");
        input7.put("account_no_in", "000000000016");
        input7.put("trans_timestamp", "1654768045000");
        input7.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input7);
        map.put(List.of("000000000016"), 100.0D);
        assertEquals(map, query.getResult());
    }

}
