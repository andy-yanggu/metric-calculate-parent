package com.yanggu.metric_calculate.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Fields;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 指标工具类单元测试类
 */
public class MetricUtilTest {

    /**
     * 测试是否校验tableData为空
     */
    @Test
    public void testInitMetricCalculate1() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> MetricUtil.initMetricCalculate(null));
        assertEquals("明细宽表为空", runtimeException.getMessage());
    }

    /**
     * 验证是否校验metricCalculate为空
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap() throws Exception {
        Method method = MetricUtil.class.getDeclaredMethod("getFieldMap", MetricCalculate.class);
        method.setAccessible(true);
        InvocationTargetException runtimeException =
                assertThrows(InvocationTargetException.class, () -> method.invoke(null, (MetricCalculate) null));
        Throwable targetException = runtimeException.getTargetException();
        assertEquals(RuntimeException.class, targetException.getClass());
        assertEquals("传入的明细宽表为空", targetException.getMessage());
    }

    /**
     * 验证是否校验fields为空
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap2() throws Exception {
        MetricCalculate metricCalculate = new MetricCalculate();
        Method method = MetricUtil.class.getDeclaredMethod("getFieldMap", MetricCalculate.class);
        method.setAccessible(true);
        InvocationTargetException runtimeException =
                assertThrows(InvocationTargetException.class, () -> method.invoke(null, metricCalculate));
        Throwable targetException = runtimeException.getTargetException();
        assertEquals(RuntimeException.class, targetException.getClass());
        assertEquals("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate), targetException.getMessage());
    }

    /**
     * 验证方法是否正常执行
     *
     * @throws Exception
     */
    @Test
    public void testGetFieldMap3() throws Exception {
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("metric_config.json"), MetricCalculate.class);
        Method method = MetricUtil.class.getDeclaredMethod("getFieldMap", MetricCalculate.class);
        method.setAccessible(true);
        Map<String, Class<?>> fieldMap = (Map<String, Class<?>>) method.invoke(null, metricCalculate);

        List<Fields> fields = metricCalculate.getFields();
        fields.forEach(tempField -> {
            assertTrue(fieldMap.containsKey(tempField.getName()));
            assertTrue(fieldMap.containsValue(tempField.getValueType().getType()));
        });

    }

    /**
     * 测试getParam方法, 测试是否校验input为空
     *
     * @throws Exception
     */
    @Test
    public void testGetParam1() throws Exception {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> MetricUtil.getParam(null, null));
        assertEquals("输入数据为空", runtimeException.getMessage());
    }

    /**
     * 测试是否校验fieldMap
     *
     * @throws Exception
     */
    @Test
    public void testGetParam2() throws Exception {
        JSONObject input = new JSONObject();
        input.set("key", "value");
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> MetricUtil.getParam(input, null));
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    /**
     * 当input中没有数据时, 应该返回null
     *
     * @throws Exception
     */
    @Test
    public void testGetParam3() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("key1", String.class);

        JSONObject input = new JSONObject();
        input.set("key", "value");

        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isEmpty(param));
        assertNull(param.get("key1"));
    }

    /**
     * 测试宽表字段是字符串, 输入其他类型数据和字符串
     *
     * @throws Exception
     */
    @Test
    public void testGetParam4() throws Exception {
        String fieldName = "key";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(fieldName, String.class);

        JSONObject input = new JSONObject();

        //输入int类型, 应该得到String类型
        int data = 1;
        input.set(fieldName, data);
        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(String.valueOf(data), param.get(fieldName));

        //输入String类型, 应该得到String类型
        input.set(fieldName, fieldName);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(fieldName, param.get(fieldName));
    }

    /**
     * 测试宽表字段是long, 输入其他类型数据和long
     *
     * @throws Exception
     */
    @Test
    public void testGetParam5() throws Exception {
        String fieldName = "key";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(fieldName, Long.class);

        JSONObject input = new JSONObject();

        //输入"1"字符串, 应该得到Long类型
        String data = "1";
        input.set(fieldName, data);
        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(Long.parseLong(data), param.get(fieldName));

        //输入1 Long类型, 应该得到Long类型
        input.set(fieldName, 1L);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(1L, param.get(fieldName));
    }

    /**
     * 测试宽表字段是BigDecimal, 输入其他类型数据和BigDecimal
     *
     * @throws Exception
     */
    @Test
    public void testGetParam6() throws Exception {
        String fieldName = "key";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(fieldName, BigDecimal.class);

        JSONObject input = new JSONObject();

        //输入"1.0"字符串, 应该得到BigDecimal类型
        String data = "1.0";
        input.set(fieldName, data);
        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(new BigDecimal(data), param.get(fieldName));

        //输入1L long类型, 应该得到BigDecimal类型
        input.set(fieldName, 1L);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(new BigDecimal(1L), param.get(fieldName));

        //输入1.0 Double, 应该得到BigDecimal类型
        input.set(fieldName, 1.0D);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(new BigDecimal("1.0"), param.get(fieldName));

        //输入1 BigDecimal类型, 应该得到BigDecimal类型
        input.set(fieldName, BigDecimal.valueOf(1L));
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(new BigDecimal(1L), param.get(fieldName));
    }

    /**
     * 测试宽表字段是boolean, 输入其他类型数据和boolean
     */
    @Test
    public void testGetParam7() {
        String fieldName = "key";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(fieldName, Boolean.class);

        JSONObject input = new JSONObject();

        //输入true字符串, 应该返回true
        input.set(fieldName, "true");
        Map<String, Object> param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(true, param.get(fieldName));

        //输入"1"字符串, 应该返回true
        input.set(fieldName, "1");
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(true, param.get(fieldName));

        //输入"0"字符串, 应该返回false
        input.set(fieldName, "0");
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(false, param.get(fieldName));

        //输入0 int, 应该返回false
        input.set(fieldName, 0);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(false, param.get(fieldName));

        //输入1 int, 应该返回true
        input.set(fieldName, 1);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(true, param.get(fieldName));

        //true, 应该返回true
        input.set(fieldName, true);
        param = MetricUtil.getParam(input, fieldMap);
        assertTrue(CollUtil.isNotEmpty(param));
        assertEquals(true, param.get(fieldName));
    }

}