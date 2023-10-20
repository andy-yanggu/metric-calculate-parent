package com.yanggu.metric_calculate.core.field_process.filter;

import com.googlecode.aviator.AviatorEvaluator;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getFilterFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 前置过滤条件字段处理器单元测试类
 */
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
     * @throws Exception
     */
    @Test
    void init2() throws Exception {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("true");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(null, aviatorExpressParam));
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 没有设置aviatorFunctionFactory应该报错
     *
     * @throws Exception
     */
    @Test
    void init3() throws Exception {
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount";
        aviatorExpressParam.setExpress(filterExpress);
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getFilterFieldProcessor(fieldMap, aviatorExpressParam, null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }


    /**
     * 表达式中使用了常量表达式, 应该报错
     *
     * @throws Exception
     */
    @Test
    void init4() throws Exception {
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
     * @throws Exception
     */
    @Test
    void init5() throws Exception {
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
     * @throws Exception
     */
    @Test
    void init6() throws Exception {
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
     *
     * @throws Exception
     */
    @Test
    void process1() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        String filterExpress = "amount > 100.00";
        aviatorExpressParam.setExpress(filterExpress);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 120);
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试字段为字符串类型
     *
     * @throws Exception
     */
    @Test
    void process2() throws Exception {
        String express = "name == '张三'";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("name", String.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", "李四");
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("name", "张三");
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试字段为整数类型
     *
     * @throws Exception
     */
    @Test
    void process3() throws Exception {
        String express = "amount > 500";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50L);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 600L);
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试字段为布尔类型
     *
     * @throws Exception
     */
    @Test
    void process4() throws Exception {
        String express = "result";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("result", Boolean.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("result", false);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("result", true);
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试逻辑运算&&、||、()、!(取反操作)
     *
     * @throws Exception
     */
    @Test
    void process5() throws Exception {
        String express = "amount > 100.00 && age >= 20";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        fieldMap.put("age", Long.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50);
        jsonObject.set("age", 21);
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 120);
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试过滤字段为BigDecimal的数据类型
     *
     * @throws Exception
     */
    @Test
    void process6() throws Exception {
        String express = "amount > 100.00";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, aviatorExpressParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", BigDecimal.valueOf(50L));
        Boolean result = filterFieldProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", BigDecimal.valueOf(120L));
        result = filterFieldProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 没有前置过滤条件, 应该返回true
     *
     * @throws Exception
     */
    @Test
    void process7() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        FilterFieldProcessor filterFieldProcessor = getFilterFieldProcessor(fieldMap, null);

        Boolean process = filterFieldProcessor.process(null);
        assertTrue(process);

    }

}
