package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.test.junit5.param.JsonParam;
import com.yanggu.metric_calculate.test.junit5.param.JsonSource;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 映射型派生指标单元测试类
 */
@DisplayName("映射型派生指标单元测试类")
class DeriveMetricsCalculateMapTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Pair<MultiFieldData, Integer>, Map<MultiFieldData, Double>, Map<List<Object>, BigDecimal>> deriveMetricCalculate;

    @BeforeAll
    static void init() {
        deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(8L);
    }

    /**
     * 测试基本映射BASEMAP
     * <p>所有交易账号的累计交易金额</p>
     */
    @ParameterizedTest
    @JsonSource("test_data.json")
    @DisplayName("测试基本映射BASEMAP")
    void testBaseMap(@JsonParam("input.accountNoIn") String accountNoIn,
                     @JsonParam("input.amount") BigDecimal amount,
                     Map<List<String>, BigDecimal> output) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account_no_out", "000000000011");
        paramMap.put("account_no_in", accountNoIn);
        paramMap.put("trans_timestamp", "1654768045000");
        paramMap.put("amount", amount);

        DeriveMetricCalculateResult<Map<List<Object>, BigDecimal>> result = deriveMetricCalculate.stateExec(paramMap);
        assertNotNull(result);
        assertNotNull(result.getResult());
        assertEquals(output, result.getResult());
    }

}
