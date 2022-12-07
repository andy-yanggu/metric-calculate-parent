package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 前置过滤条件字段处理器单元测试类
 */
public class FilterProcessorTest {

    /**
     * 如果没有设置前置过滤条件和fieldMap应该正常执行
     *
     * @throws Exception
     */
    @Test
    public void init1() throws Exception {
        FilterProcessor filterProcessor = new FilterProcessor();
        filterProcessor.init();

        assertNull(filterProcessor.getFilterExpression());
    }

    /**
     * 表达式中使用了常量表达式, 应该报错
     *
     * @throws Exception
     */
    @Test
    public void init2() throws Exception {
        String filterExpress = "true";

        FilterProcessor filterProcessor = new FilterProcessor();
        filterProcessor.setFieldMap(Collections.emptyMap());
        filterProcessor.setFilterExpress(filterExpress);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterProcessor::init);

        assertEquals("过滤条件为常量表达式, 没有意义: " + filterExpress, runtimeException.getMessage());
    }

    /**
     * 表达式中使用了数据明细宽表中没有的字段, 应该报错
     *
     * @throws Exception
     */
    @Test
    public void init3() throws Exception {
        String filterExpress = "amount > 100.00";

        FilterProcessor filterProcessor = new FilterProcessor();
        filterProcessor.setFilterExpress(filterExpress);
        filterProcessor.setFieldMap(Collections.emptyMap());
        RuntimeException runtimeException = assertThrows(RuntimeException.class, filterProcessor::init);

        assertEquals("数据明细宽表中没有该字段: amount", runtimeException.getMessage());
    }

    /**
     * 表达式正常, 应该正常编译
     *
     * @throws Exception
     */
    @Test
    public void init4() throws Exception {
        String filterExpress = "amount > 100.00";

        FilterProcessor filterProcessor = new FilterProcessor();
        filterProcessor.setFilterExpress(filterExpress);
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        filterProcessor.setFieldMap(fieldMap);
        filterProcessor.init();

        assertEquals(AviatorEvaluator.compile(filterExpress, true).toString(), filterProcessor.getFilterExpression().toString());
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

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50);
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 120);
        result = filterProcessor.process(jsonObject);
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

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("name", "李四");
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("name", "张三");
        result = filterProcessor.process(jsonObject);
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

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50L);
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 600L);
        result = filterProcessor.process(jsonObject);
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

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("result", false);
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("result", true);
        result = filterProcessor.process(jsonObject);
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

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 50);
        jsonObject.set("age", 21);
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", 120);
        result = filterProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 测试过滤字段为BigDecimal的数据类型
     * 但是传入的数据类型和定义的不匹配, 代码内部进行了转换
     *
     * @throws Exception
     */
    @Test
    public void process6() throws Exception {
        String express = "amount > 100.00";
        Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};

        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, express);
        filterProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", "50");
        Boolean result = filterProcessor.process(jsonObject);
        assertFalse(result);

        jsonObject.set("amount", "120");
        result = filterProcessor.process(jsonObject);
        assertTrue(result);
    }

    /**
     * 没有前置过滤条件, 应该返回true
     *
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        FilterProcessor filterProcessor = new FilterProcessor();
        filterProcessor.init();

        Boolean process = filterProcessor.process(null);
        assertTrue(process);

    }

}
