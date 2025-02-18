package com.yanggu.metric_calculate.core.field_process.filter;

import com.googlecode.aviator.AviatorEvaluator;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getFilterFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 前置过滤条件字段处理器单元测试类
 */
@DisplayName("前置过滤条件字段处理器单元测试类")
class FilterFieldProcessorTest {

    /**
     * 如果没有设置前置过滤条件应该正常执行
     *
     * @throws Exception
     */
    @Test
    void init1() throws Exception {
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(null, null);
        filterFieldProcessor.init();

        assertNull(filterFieldProcessor.getFilterExpression());
    }

    /**
     * 如果设置了过滤表达式, 但是没有设置fieldMap应该应该报错
     *
     */
    @Test
    void init2() {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("true");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(null, aviatorExpressParam));
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 没有设置aviatorFunctionFactory应该报错
     *
     */
    @Test
    void init3() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount";
        aviatorExpressParam.setExpress(filterExpress);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(fieldMap, aviatorExpressParam, null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }


    /**
     * 表达式中使用了常量表达式, 应该报错
     *
     */
    @Test
    void init4() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "true";
        aviatorExpressParam.setExpress(filterExpress);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(fieldMap, aviatorExpressParam));
        assertEquals("过滤条件为常量表达式, 没有意义: " + filterExpress, runtimeException.getMessage());
    }

    /**
     * 表达式中使用了数据明细宽表中没有的字段, 应该报错
     *
     */
    @Test
    void init5() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount2", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount > 100.00";
        aviatorExpressParam.setExpress(filterExpress);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(fieldMap, aviatorExpressParam));

        assertEquals("数据明细宽表中没有该字段: amount", runtimeException.getMessage());
    }

    /**
     * 表达式正常, 应该正常编译
     *
     */
    @Test
    void init6() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount > 100.00";
        aviatorExpressParam.setExpress(filterExpress);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        assertEquals(AviatorEvaluator.compile(filterExpress, true).toString(), filterFieldProcessor.getFilterExpression().toString());
        assertEquals(aviatorExpressParam, filterFieldProcessor.getFilterExpressParam());
        assertEquals(fieldMap, filterFieldProcessor.getFieldMap());
    }

    /**
     * 测试过滤字段为BigDecimal的数据类型
     */
    @ParameterizedTest
    @CsvSource({"50,false", "120,true"})
    @DisplayName("测试过滤字段为BigDecimal的数据类型")
    void process1(BigDecimal amount, Boolean expected) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount > 100.00";
        aviatorExpressParam.setExpress(filterExpress);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("amount", amount);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertEquals(expected, result);
    }

    /**
     * 测试字段为字符串类型
     */
    @ParameterizedTest
    @DisplayName("测试字段为字符串类型")
    @CsvSource({"'张三',true", "'李四',false"})
    void process2(String name, Boolean expected) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String express = "name == '张三'";
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("name", name);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertEquals(expected, result);
    }

    /**
     * 测试字段为整数类型
     */
    @ParameterizedTest
    @DisplayName("测试字段为整数类型")
    @CsvSource({"500,false", "600,true"})
    void process3(Long amount, Boolean expected) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String express = "amount > 500";
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("amount", amount);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertEquals(expected, result);
    }

    /**
     * 测试字段为布尔类型
     */
    @ParameterizedTest
    @DisplayName("测试字段为布尔类型")
    @CsvSource({"true,true", "false,false"})
    void process4(Boolean input, Boolean expected) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("result", Boolean.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String express = "result";
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("result", input);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertEquals(expected, result);
    }

    /**
     * 测试逻辑运算&&、||、()、!(取反操作)
     */
    @ParameterizedTest
    @DisplayName("测试逻辑运算&&、||、()、!(取反操作)")
    @CsvSource({"50,21,false", "120,21,true", "120,20,true"})
    void process5(BigDecimal amount, Long age, Boolean expected) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        fieldMap.put("age", Long.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String express = "amount > 100.00 && age >= 20";
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("amount", amount);
        jsonObject.put("age", age);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertEquals(expected, result);
    }

    /**
     * 没有前置过滤条件, 应该返回true
     */
    @Test
    @DisplayName("没有前置过滤条件, 应该返回true")
    void process6() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, null);

        Boolean process = filterFieldProcessor.process(null);
        assertTrue(process);
    }

}
