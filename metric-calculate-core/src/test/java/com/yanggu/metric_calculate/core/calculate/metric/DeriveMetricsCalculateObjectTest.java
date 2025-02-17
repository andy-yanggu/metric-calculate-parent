package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 对象型派生指标单元测试类
 */
class DeriveMetricsCalculateObjectTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Pair<MultiFieldData, Long>, MutableObj<Pair<MultiFieldData, Long>>, Long> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(4L);
    }

    /**
     * 测试对象型MAXFIELD
     * <p>最大交易的金额的交易时间戳</p>
     */
    @DisplayName("对象型MAXFIELD")
    @ParameterizedTest
    @CsvSource(
            {
                    "1654768045000,800,1654768045000",
                    "1654768046000,900,1654768046000",
                    "1654768045000,800,1654768046000",
                    "1354768045000,1100,1354768045000"
            }
    )
    void testMaxField(Long transTimestamp, Long amount, Long expectedTimestamp) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account_no_in", "000000000012");
        paramMap.put("trans_timestamp", transTimestamp);
        paramMap.put("amount", amount);

        DeriveMetricCalculateResult<Long> query = deriveMetricCalculate.stateExec(paramMap);
        assertEquals(expectedTimestamp, query.getResult());
    }

}
