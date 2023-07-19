package com.yanggu.metric_calculate.core2.calculate.field;

import cn.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 真实字段处理器单元测试类
 */
class RealFieldCalculateTest {

    @Test
    void testInit() {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, realFieldCalculate::init);
        assertEquals("字段名为空", runtimeException.getMessage());

        realFieldCalculate.setColumnName("testColumn");
        runtimeException = assertThrows(RuntimeException.class, realFieldCalculate::init);
        assertEquals("字段数据类型为空", runtimeException.getMessage());

        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();
        assertEquals("testColumn", realFieldCalculate.getColumnName());
        assertEquals(String.class, realFieldCalculate.getDataClass());
    }

    @Test
    void testGetName() {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        assertNull(realFieldCalculate.getName());

        realFieldCalculate.setColumnName("testColumn");
        assertEquals("testColumn", realFieldCalculate.getName());
    }

    @Test
    void process1() throws Exception {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> realFieldCalculate.process(null));
        assertEquals("传入的数据为空", runtimeException.getMessage());
    }

    @Test
    void process2() throws Exception {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        JSONObject input = new JSONObject();
        input.set("test2", "test3");
        String process = realFieldCalculate.process(input);
        assertNull(process);
    }

    @Test
    void process3() throws Exception {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        JSONObject input = new JSONObject();
        input.set("test", "haha");
        String process = realFieldCalculate.process(input);
        assertEquals("haha", process);
    }

    /**
     * 测试宽表字段是字符串, 输入其他类型数据和字符串
     *
     * @throws Exception
     */
    @Test
    void process4() throws Exception {
        String fieldName = "key";
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName(fieldName);
        realFieldCalculate.setDataClass(String.class);

        int data = 1;
        JSONObject input = new JSONObject();

        //输入int类型, 应该得到String类型
        input.set(fieldName, data);
        String process = realFieldCalculate.process(input);
        assertEquals(String.valueOf(data), process);

        //输入String类型, 应该得到String类型
        input.set(fieldName, fieldName);
        process = realFieldCalculate.process(input);
        assertEquals(fieldName, process);
    }

    /**
     * 测试宽表字段是long, 输入其他类型数据和long
     *
     * @throws Exception
     */
    @Test
    void process5() throws Exception {
        String fieldName = "key";
        RealFieldCalculate<Long> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName(fieldName);
        realFieldCalculate.setDataClass(Long.class);

        JSONObject input = new JSONObject();
        String data = "1";

        //输入"1"字符串, 应该得到Long类型
        input.set(fieldName, data);
        Long process = realFieldCalculate.process(input);
        assertEquals(new Long(data), process);

        //输入1 Long类型, 应该得到Long类型
        input.set(fieldName, 1L);
        process = realFieldCalculate.process(input);
        assertEquals(new Long(data), process);
        assertEquals(1L, process.longValue());
    }

    /**
     * 测试宽表字段是BigDecimal, 输入其他类型数据和BigDecimal
     *
     * @throws Exception
     */
    @Test
    void process6() throws Exception {
        String fieldName = "key";
        RealFieldCalculate<BigDecimal> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName(fieldName);
        realFieldCalculate.setDataClass(BigDecimal.class);

        JSONObject input = new JSONObject();

        //输入"1.0"字符串, 应该得到BigDecimal类型
        String data = "1.0";
        input.set(fieldName, data);
        BigDecimal process = realFieldCalculate.process(input);
        assertEquals(new BigDecimal(data), process);

        //输入1L long类型, 应该得到BigDecimal类型
        input.set(fieldName, 1L);
        process = realFieldCalculate.process(input);
        assertEquals(new BigDecimal(1L), process);

        //输入1.0 Double, 应该得到BigDecimal类型
        input.set(fieldName, 1.0D);
        process = realFieldCalculate.process(input);
        assertEquals(new BigDecimal("1.0"), process);

        //输入1 BigDecimal类型, 应该得到BigDecimal类型
        input.set(fieldName, BigDecimal.valueOf(1L));
        process = realFieldCalculate.process(input);
        assertEquals(new BigDecimal(1L), process);
    }

    /**
     * 测试宽表字段是boolean, 输入其他类型数据和boolean
     */
    @Test
    void process7()  throws Exception {
        String fieldName = "key";
        RealFieldCalculate<Boolean> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName(fieldName);
        realFieldCalculate.setDataClass(Boolean.class);

        JSONObject input = new JSONObject();

        //输入true字符串, 应该返回true
        input.set(fieldName, "true");
        Boolean process = realFieldCalculate.process(input);
        assertEquals(true, process);

        //输入"1"字符串, 应该返回true
        input.set(fieldName, "1");
        process = realFieldCalculate.process(input);
        assertEquals(true, process);

        //输入"0"字符串, 应该返回false
        input.set(fieldName, "0");
        process = realFieldCalculate.process(input);
        assertEquals(false, process);

        //输入0 int, 应该返回false
        input.set(fieldName, 0);
        process = realFieldCalculate.process(input);
        assertEquals(false, process);

        //输入1 int, 应该返回true
        input.set(fieldName, 1);
        process = realFieldCalculate.process(input);
        assertEquals(true, process);

        //true, 应该返回true
        input.set(fieldName, true);
        process = realFieldCalculate.process(input);
        assertEquals(true, process);
    }

}