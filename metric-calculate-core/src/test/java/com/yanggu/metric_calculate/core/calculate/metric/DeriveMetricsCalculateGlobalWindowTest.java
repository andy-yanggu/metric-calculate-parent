package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 全窗口派生指标单元测试类
 */
@DisplayName("全窗口派生指标单元测试类")
class DeriveMetricsCalculateGlobalWindowTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Long, MutableObj<Long>, Long> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(12L);
    }

    /**
     * FIRSTFIELD: 占位，首次交易时间
     */
    @DisplayName("FIRSTFIELD: 占位，首次交易时间")
    @ParameterizedTest
    @CsvSource({"1654768045000,1654768045000", "1654768045001,1654768045000", "1654768045002,1654768045000"})
    void testOccupiedField(Long transTimestamp, Long expectedTimestamp) {
        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", transTimestamp);
        input1.put("amount", 800);

        DeriveMetricCalculateResult<Long> query = deriveMetricCalculate.stateExec(input1);
        assertNotNull(query);
        assertEquals(expectedTimestamp, query.getResult());
    }

}
