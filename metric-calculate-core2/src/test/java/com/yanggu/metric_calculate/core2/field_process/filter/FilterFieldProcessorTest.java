package com.yanggu.metric_calculate.core2.field_process.filter;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 前置过滤条件字段处理器单元测试类
 */
public class FilterFieldProcessorTest {

    /**
     * 如果没有设置前置过滤条件和fieldMap应该正常执行
     *
     * @throws Exception
     */
    @Test
    public void init1() throws Exception {
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
    public void init2() throws Exception {
        String filterExpress = "true";
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        filterFieldProcessor.setFilterExpress(filterExpress);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 表达式中使用了常量表达式, 应该报错
     *
     * @throws Exception
     */
    @Test
    public void init3() throws Exception {
        String filterExpress = "true";

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);
        filterFieldProcessor.setFilterExpress(filterExpress);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);

        assertEquals("过滤条件为常量表达式, 没有意义: " + filterExpress, runtimeException.getMessage());
    }

    /**
     * 表达式中使用了数据明细宽表中没有的字段, 应该报错
     *
     * @throws Exception
     */
    @Test
    public void init4() throws Exception {
        String filterExpress = "amount > 100.00";

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        filterFieldProcessor.setFilterExpress(filterExpress);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount2", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterFieldProcessor::init);

        assertEquals("数据明细宽表中没有该字段: amount", runtimeException.getMessage());
    }

    /**
     * 表达式正常, 应该正常编译
     *
     * @throws Exception
     */
    @Test
    public void init5() throws Exception {
        String filterExpress = "amount > 100.00";

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        filterFieldProcessor.setFilterExpress(filterExpress);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        filterFieldProcessor.setFieldMap(fieldMap);
        filterFieldProcessor.init();

        assertEquals(AviatorEvaluator.compile(filterExpress, true).toString(), filterFieldProcessor.getFilterExpression().toString());
        assertEquals(filterExpress, filterFieldProcessor.getFilterExpress());
        assertEquals(fieldMap, filterFieldProcessor.getFieldMap());
    }

    /**
     * 测试过滤字段为BigDecimal的数据类型
     *
     * @throws Exception
     */
    @Test
    public void process1() throws Exception {
        String express = "amount > 100.00";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void process2() throws Exception {
        String express = "name == '张三'";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("name", String.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void process3() throws Exception {
        String express = "amount > 500";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", Long.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void process4() throws Exception {
        String express = "result";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("result", Boolean.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void process5() throws Exception {
        String express = "amount > 100.00 && age >= 20";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
            put("age", Long.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void process6() throws Exception {
        String express = "amount > 100.00";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};

        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, express);
        filterFieldProcessor.init();

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
    public void test6() throws Exception {
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        filterFieldProcessor.init();

        Boolean process = filterFieldProcessor.process(null);
        assertTrue(process);

    }

}
