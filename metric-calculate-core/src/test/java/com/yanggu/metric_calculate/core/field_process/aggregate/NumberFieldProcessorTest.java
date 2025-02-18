package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.UdafParamTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * NumberFieldProcessor单元测试类
 * <p>数值型聚合字段处理器单元测试类</p>
 */
@DisplayName("NumberFieldProcessor单元测试类")
class NumberFieldProcessorTest {

    private static FieldProcessor<Map<String, Object>, Double> sumNumberFieldProcessor;

    private static FieldProcessor<Map<String, Object>, MultiFieldData> covNumberFieldProcessor;

    @BeforeAll
    static void init() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("amount2", Double.class);

        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SUM", "amount");
        sumNumberFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        BaseUdafParam baseUdafParam2 = UdafParamTestBase.createBaseUdafParam("COV", null, "amount", "amount2");
        covNumberFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam2);
    }

    /**
     * 测试SUM
     */
    @ParameterizedTest
    @DisplayName("测试SUM")
    @CsvSource(value = {"100.0, 100.0", "200.0, 200.0", "300.0, 300.0"})
    void testProcess1(Double amount, Double expected) throws Exception {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("amount", amount);

        Double process = sumNumberFieldProcessor.process(inputParam);
        assertEquals(expected, process, 0.0D);
    }

    /**
     * 测试协方差COV
     */
    @ParameterizedTest
    @DisplayName("测试COV")
    @CsvSource(value = {"100.0,200.0,100.0,200.0", "150.0,300.0,150.0,300.0", "200.0,400.0,200.0,400.0"})
    void testProcess2(Double amount1, Double amount2, Double expected1, Double expected2) throws Exception {
        Map<String, Object> inputParam = new HashMap<>();
        inputParam.put("amount", amount1);
        inputParam.put("amount2", amount2);

        MultiFieldData process = covNumberFieldProcessor.process(inputParam);
        assertNotNull(process);
        assertEquals(2, process.getFieldList().size());
        assertEquals(expected1, process.getFieldList().get(0));
        assertEquals(expected2, process.getFieldList().get(1));
    }

}