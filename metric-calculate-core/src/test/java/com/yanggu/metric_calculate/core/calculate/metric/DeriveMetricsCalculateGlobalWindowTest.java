package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 全窗口派生指标单元测试类
 */
class DeriveMetricsCalculateGlobalWindowTest extends DeriveMetricsCalculateBase {

    /**
     * 占位，首次交易时间
     */
    @Test
    void testOccupiedField() throws Exception {
        DeriveMetricCalculate<Long, MutableObj<Long>, Long> deriveMetricCalculate =
                metricCalculate.getDeriveMetricCalculateById(12L);

        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", 1654768045000L);
        input1.put("amount", 800);

        DeriveMetricCalculateResult<Long> query = deriveMetricCalculate.stateExec(input1);
        assertEquals(1654768045000L, query.getResult().longValue());

        Map<String, Object> input2 = new HashMap<>();
        input2.put("account_no_out", "000000000011");
        input2.put("account_no_in", "000000000012");
        input2.put("trans_timestamp", 1654768045001L);
        input2.put("amount", 800);
        query = deriveMetricCalculate.stateExec(input2);
        assertEquals(1654768045000L, query.getResult().longValue());

        Map<String, Object> input3 = new HashMap<>();
        input3.put("account_no_out", "000000000011");
        input3.put("account_no_in", "000000000012");
        input3.put("trans_timestamp", 1654768045002L);
        input3.put("amount", 800);
        query = deriveMetricCalculate.stateExec(input3);
        assertEquals(1654768045000L, query.getResult().longValue());
    }

}
