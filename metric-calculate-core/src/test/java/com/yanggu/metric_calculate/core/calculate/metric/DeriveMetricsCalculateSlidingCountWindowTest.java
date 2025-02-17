package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 滑动计数求和派生指标单元测试类
 */
@DisplayName("滑动计数窗口派生指标单元测试类")
class DeriveMetricsCalculateSlidingCountWindowTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Integer, Double, Double> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(11L);
    }

    /**
     * 测试滑动计数窗口limit限制为5, 最近5笔交易, 进行求和
     */
    @DisplayName("滑动计数窗口limit限制为5, 最近5笔交易, 进行求和")
    @ParameterizedTest
    @CsvSource(
            {
                    "800.0,800.0",
                    "900.0,1700.0",
                    "1000.0,2700.0",
                    "1100.0,3800.0",
                    "100.0,3900.0",
                    "100.0,3200.0",
                    "100.0,2400.0"
            }
    )
    void testSum(Double input, Double expected) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account_no_out", "000000000011");
        paramMap.put("account_no_in", "000000000012");
        paramMap.put("trans_timestamp", "1654768045000");
        paramMap.put("amount", input);

        DeriveMetricCalculateResult<Double> query = deriveMetricCalculate.stateExec(paramMap);
        assertEquals(expected, query.getResult(), 0.0D);
    }

}
