package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.UdafParamTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * NumberFieldProcessor单元测试类
 * <p>数值型聚合字段处理器单元测试类</p>
 */
class NumberFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() {
        this.fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("amount1", Long.class);
    }

    @Test
    void testProcess1() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SUM", "amount");
        FieldProcessor<Map<String, Object>, Double> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("amount", 100.0D);
        Double process = baseFieldProcessor.process(jsonObject);
        assertEquals(100.0D, process, 0.0D);
    }

    /**
     * 测试协方差CovUnit
     *
     * @throws Exception
     */
    @Test
    void testProcess_CovUnit() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("COV", null,  "amount", "amount1");
        FieldProcessor<Map<String, Object>, MultiFieldData> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        Map<String, Object> input = new HashMap<>();
        input.put("amount", 1L);
        input.put("amount1", 2L);
        MultiFieldData process = baseFieldProcessor.process(input);
        assertEquals(2, process.getFieldList().size());
        assertEquals(1L, process.getFieldList().get(0));
        assertEquals(2L, process.getFieldList().get(1));
    }

}