package com.yanggu.metric_calculate.core.field_process.filter;

import com.googlecode.aviator.AviatorEvaluator;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFactoryTest.getAviatorFunctionFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 前置过滤条件字段处理器单元测试类
 */
class FilterFieldProcessorTest {

    /**
     * 如果没有设置前置过滤条件和fieldMap应该正常执行
     *
     * @throws Exception
     */
    @Test
    void init1() throws Exception {
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
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
        String filterExpress = "true";
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        filterFieldProcessor.setFilterExpressParam(aviatorExpressParam);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 没有设置aviatorFunctionFactory应该报错
     *
     * @throws Exception
     */
    @Test
    void init3() throws Exception {
        String filterExpress = "abc";
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        filterFieldProcessor.setFilterExpressParam(aviatorExpressParam);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }


    /**
     * 表达式中使用了常量表达式, 应该报错
     *
     * @throws Exception
     */
    @Test
    void init4() throws Exception {
        String filterExpress = "true";

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        filterFieldProcessor.setFilterExpressParam(aviatorExpressParam);
        filterFieldProcessor.setAviatorFunctionFactory(getAviatorFunctionFactory());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);

        assertEquals("过滤条件为常量表达式, 没有意义: " + filterExpress, runtimeException.getMessage());
    }

    /**
     * 表达式中使用了数据明细宽表中没有的字段, 应该报错
     *
     * @throws Exception
     */
    @Test
    void init5() throws Exception {
        String filterExpress = "amount > 100.00";

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        filterFieldProcessor.setFilterExpressParam(aviatorExpressParam);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount2", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);
        filterFieldProcessor.setAviatorFunctionFactory(getAviatorFunctionFactory());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);

        assertEquals("数据明细宽表中没有该字段: amount", runtimeException.getMessage());
    }

    /**
     * 表达式正常, 应该正常编译
     *
     * @throws Exception
     */
    @Test
    void init6() throws Exception {
        String filterExpress = "amount > 100.00";

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(filterExpress);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        String express = "amount > 100.00";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", Long.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("result", Boolean.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
            put("age", Long.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress(express);
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, aviatorExpressParam);

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
    void test6() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        FilterFieldProcessor filterFieldProcessor = FieldProcessorTestBase.getFilterFieldProcessor(fieldMap, null);

        Boolean process = filterFieldProcessor.process(null);
        assertTrue(process);

    }

}
