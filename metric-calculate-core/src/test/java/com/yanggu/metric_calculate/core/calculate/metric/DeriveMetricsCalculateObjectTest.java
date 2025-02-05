package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", 800);

        query = deriveMetricCalculate.stateExec(input1);
        assertEquals("1654768045000", query.getResult());

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", "1654768046000");
        input2.put("amount", 900);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals("1654768046000", query.getResult());

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_in", "000000000012");
        input3.put("trans_timestamp", "1654768045000");
        input3.put("amount", 800);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals("1654768046000", query.getResult());

        Map<String, Object> input4 = new HashMap<>();
        input4.put("account_no_in", "000000000012");
        input4.put("trans_timestamp", "1354768045000");
        input4.put("amount", 1100);
        query = deriveMetricCalculate.stateExec(input4);
        assertEquals("1354768045000", query.getResult());
    }

}
