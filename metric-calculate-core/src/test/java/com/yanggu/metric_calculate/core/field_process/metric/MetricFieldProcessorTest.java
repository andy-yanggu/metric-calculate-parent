package com.yanggu.metric_calculate.core.field_process.metric;

import com.googlecode.aviator.AviatorEvaluator;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMetricFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 度量字段处理器单元测试类
 */
@DisplayName("度量字段处理器单元测试类")
class MetricFieldProcessorTest {

    /**
     * 没有传递fieldMap, 应该报错
     */
    @Test
    void init1() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricFieldProcessor(null, null));
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 没有传递metricExpress, 应该报错
     */
    @Test
    void init2() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount2", BigDecimal.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricFieldProcessor(fieldMap, null));
        assertEquals("Aviator表达式配置为空", runtimeException.getMessage());

        runtimeException = assertThrows(RuntimeException.class, () -> getMetricFieldProcessor(fieldMap, new AviatorExpressParam()));
        assertEquals("Aviator表达式配置为空", runtimeException.getMessage());
    }

    /**
     * 没有传递AviatorFunctionFactory应该报错
     */
    @Test
    void init3() {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount2", BigDecimal.class);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, aviatorExpressParam, null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }

    /**
     * 如果度量表达式的变量没有在宽表字段中, 应该报错
     *
     */
    @Test
    void init4() {
        String metricExpress = "amount";
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(metricExpress);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount2", BigDecimal.class);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricFieldProcessor(fieldMap, aviatorExpressParam));
        assertEquals("数据明细宽表中没有该字段: " + metricExpress, runtimeException.getMessage());

    }

    /**
     * init方法应该对度量值进行编译
     *
     */
    @Test
    void init5() {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        MetricFieldProcessor<Object> objectMetricFieldProcessor = getMetricFieldProcessor(fieldMap, aviatorExpressParam);

        assertEquals(AviatorEvaluator.compile("amount", true).toString(), objectMetricFieldProcessor.getMetricExpression().toString());
        assertEquals(aviatorExpressParam, objectMetricFieldProcessor.getAviatorExpressParam());
        assertEquals(fieldMap, objectMetricFieldProcessor.getFieldMap());

    }

    /**
     * 从明细数据中取出度量值
     */
    @ParameterizedTest
    @DisplayName("从明细数据中取出度量值")
    @ValueSource(doubles = {100.0D, 200.1D, 300.2D})
    void process(Double amount) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        MetricFieldProcessor<Double> objectMetricFieldProcessor = getMetricFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("amount", amount);

        Double process = objectMetricFieldProcessor.process(paramMap);
        assertEquals(amount, process);
    }

}