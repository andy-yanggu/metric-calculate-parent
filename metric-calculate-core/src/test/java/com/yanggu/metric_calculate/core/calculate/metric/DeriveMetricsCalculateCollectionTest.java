package com.yanggu.metric_calculate.core.calculate.metric;


import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 集合型派生指标单元测试类
 */
@DisplayName("集合型派生指标单元测试类")
class DeriveMetricsCalculateCollectionTest extends DeriveMetricsCalculateBase {

    private static DeriveMetricCalculate<Map<String, Object>, List<Map<String, Object>>, List<Map<String, Object>>> listObjectDeriveMetricCalculate;

    private static DeriveMetricCalculate<Pair<MultiFieldData, Map<String, Object>>, BoundedPriorityQueue<Pair<MultiFieldData, Map<String, Object>>>, List<Map<String, Object>>> sortListObjectDeriveMetricCalculate;

    @BeforeAll
    static void init() {
        listObjectDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(6L);
        sortListObjectDeriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(7L);
    }

    /**
     * 测试LISTOBJECT类型, limit限制为5, 最多只能存储5个
     */
    @ParameterizedTest
    @CsvSource(
            {
                    "800,'[800]'",
                    "900,'[800,900]'",
                    "1000,'[800,900,1000]'",
                    "1100,'[800,900,1000,1100]'",
                    "1200,'[800,900,1000,1100,1200]'",
                    "1300,'[800,900,1000,1100,1200]'",
                    "1400,'[800,900,1000,1100,1200]'"
            }
    )
    @DisplayName("LISTOBJECT类型, limit限制为5, 最多只能存储5个")
    void testListObject(Double amount, String jsonArray) {
        Map<String, Object> paramMap = buildParamMap(amount);

        DeriveMetricCalculateResult<List<Map<String, Object>>> query = listObjectDeriveMetricCalculate.stateExec(paramMap);
        assertNotNull(query);
        assertEquals(buildMapList(jsonArray), query.getResult());
    }

    /**
     * 测试SORTEDLIMITLISTOBJECT有序列表对象, 按照amount升序排序, 取3个
     */
    @ParameterizedTest
    @CsvSource(
            {
                    "800,'[800]'",
                    "900,'[800,900]'",
                    "1000,'[800,900,1000]'",
                    "700,'[700,800,900]'",
                    "1200,'[700,800,900]'",
                    "600,'[600,700,800]'",
                    "600,'[600,600,700]'"
            }
    )
    @DisplayName("SORTEDLIMITLISTOBJECT有序列表对象, 按照amount升序排序, 取3个")
    void testSort_list_object(Double amount, String jsonArray) {
        Map<String, Object> input1 = new HashMap<>();
        input1.put("account_no_out", "000000000011");
        input1.put("account_no_in", "000000000012");
        input1.put("trans_timestamp", "1654768045000");
        input1.put("amount", amount);

        DeriveMetricCalculateResult<List<Map<String, Object>>> listDeriveMetricCalculateResult = sortListObjectDeriveMetricCalculate.stateExec(input1);
        assertNotNull(listDeriveMetricCalculateResult);
        List<Map<String, Object>> stateExec = listDeriveMetricCalculateResult.getResult();
        assertEquals(buildMapList(jsonArray), stateExec);
    }

    private List<Map<String, Object>> buildMapList(String doubleJsonArrayString) {
        return JSONUtil.toList(doubleJsonArrayString, Double.class)
                .stream()
                .map(this::buildParamMap)
                .toList();
    }

    private Map<String, Object> buildParamMap(Double amount) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("account_no_out", "000000000011");
        paramMap.put("account_no_in", "000000000012");
        paramMap.put("trans_timestamp", "1654768045000");
        paramMap.put("amount", amount);
        return paramMap;
    }

}
