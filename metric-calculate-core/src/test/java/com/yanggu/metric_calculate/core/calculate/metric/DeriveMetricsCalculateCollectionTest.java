package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 集合型派生指标单元测试类
 */
class DeriveMetricsCalculateCollectionTest extends DeriveMetricsCalculateBase {

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @Test
    void testListObject() {
        DeriveMetricCalculate<Map<String, Object>, List<Map<String, Object>>, List<Map<String, Object>>> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(6L);

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", "800");

        DeriveMetricCalculateResult<List<Map<String, Object>>> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(Collections.singletonList(input1), query.getResult());

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", "1654768045000");
        input2.put("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(List.of(input1, input2), query.getResult());

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "000000000012");
        input3.put("trans_timestamp", "1654768045000");
        input3.put("amount", 1000);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(List.of(input1, input2, input3), query.getResult());

        Map<String, Object> input4 = new HashMap<>();
        input4.put("account_no_out", "000000000011");
        input4.put("account_no_in", "000000000012");
        input4.put("trans_timestamp", "1654768045000");
        input4.put("amount", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals(List.of(input1, input2, input3, input4), query.getResult());

        Map<String, Object> input5 = new HashMap<>();
        input5.put("account_no_out", "000000000011");
        input5.put("account_no_in", "000000000012");
        input5.put("trans_timestamp", "1654768045000");
        input5.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input5);
        assertEquals(List.of(input1, input2, input3, input4, input5), query.getResult());

        Map<String, Object> input6 = new HashMap<>();
        input6.put("account_no_out", "000000000011");
        input6.put("account_no_in", "000000000012");
        input6.put("trans_timestamp", "1654768045000");
        input6.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input6);
        assertEquals(List.of(input1, input2, input3, input4, input5), query.getResult());

        Map<String, Object> input7 = new HashMap<>();
        input7.put("account_no_out", "000000000011");
        input7.put("account_no_in", "000000000012");
        input7.put("trans_timestamp", "1654768045000");
        input7.put("amount", 100);
        query = deriveMetricCalculate.stateExec(input7);
        assertEquals(List.of(input1, input2, input3, input4, input5), query.getResult());
    }

    /**
     * 测试有序列表对象, 按照amount升序排序, 取5个
     * <p>SORTEDLIMITLISTOBJECT</p>
     */
    @Test
    void testSort_list_object() throws Exception {
        DeriveMetricCalculate<Pair<MultiFieldData, Map<String, Object>>, BoundedPriorityQueue<Pair<MultiFieldData, Map<String, Object>>>, List<Map<String, Object>>> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(7L);
        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", 800);

        List<Map<String, Object>> stateExec = deriveMetricCalculate.stateExec(input1).getResult();
        assertEquals(1, stateExec.size());
        Map<String, Object> actual = stateExec.getFirst();
        assertEquals(input1, actual);

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", "1654768045000");
        input2.put("amount", 900);
        stateExec = deriveMetricCalculate.stateExec(input2).getResult();
        assertEquals(2, stateExec.size());
        assertEquals(input1, stateExec.get(0));
        assertEquals(input2, stateExec.get(1));
    }

}
