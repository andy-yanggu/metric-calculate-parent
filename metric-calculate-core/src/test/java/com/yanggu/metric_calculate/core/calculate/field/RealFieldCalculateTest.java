package com.yanggu.metric_calculate.core.calculate.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 真实字段处理器单元测试类
 */
@DisplayName("真实字段处理器单元测试类")
class RealFieldCalculateTest {

    @Test
    @DisplayName("测试宽表字段赋值和数据类型是否正常")
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
    @DisplayName("测试宽表字段赋值是否正常")
    void testGetName() {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        assertNull(realFieldCalculate.getName());

        realFieldCalculate.setColumnName("testColumn");
        assertEquals("testColumn", realFieldCalculate.getName());
    }

    @Test
    @DisplayName("测试传入的数据为空, 应该抛出异常")
    void process1() {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> realFieldCalculate.process(null));
        assertEquals("传入的数据为空", runtimeException.getMessage());
    }

    @Test
    @DisplayName("测试传入的数据中, 字段名不存在, 应该返回null")
    void process2() throws Exception {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        Map<String, Object> input = new HashMap<>();
        input.put("test2", "test3");

        String process = realFieldCalculate.process(input);
        assertNull(process);
    }

    @Test
    @DisplayName("测试传入的数据中, 字段名存在, 应该返回字段值")
    void process3() throws Exception {
        RealFieldCalculate<String> realFieldCalculate = new RealFieldCalculate<>();
        realFieldCalculate.setColumnName("test");
        realFieldCalculate.setDataClass(String.class);
        realFieldCalculate.init();

        Map<String, Object> input = new HashMap<>();
        input.put("test", "haha");

        String process = realFieldCalculate.process(input);
        assertEquals("haha", process);
    }

    /**
     * 测试宽表字段是字符串, 输入其他类型数据和字符串
     * <p>输入"haha"字符串, 应该得到"haha"
     * <p>输入1 int类型, 应该得到String类型
     */
    @ParameterizedTest
    @CsvSource({"'haha','haha'", "1,'1'"})
    @DisplayName("测试宽表字段是字符串类型, 输入其他类型数据和字符串, 应该得到字符串")
    void process4(Object inputParam, String expectValue) throws Exception {
        String process = process(String.class, inputParam);
        assertEquals(expectValue, process);
    }

    /**
     * 测试宽表字段是long, 输入其他类型数据和long
     * <p>输入"1"字符串, 应该得到1L
     * <p>输入1 Long类型, 应该得到1L
     */
    @ParameterizedTest
    @CsvSource({"'1',1", "2,2"})
    @DisplayName("测试宽表字段是long类型, 输入其他类型数据和long, 应该得到long")
    void process5(Object inputParam, Long expectValue) throws Exception {
        Long process = process(Long.class, inputParam);
        assertEquals(expectValue, process);
    }

    /**
     * 测试宽表字段是BigDecimal, 输入其他类型数据和BigDecimal
     * <p>输入"1.0"字符串, 应该得到BigDecimal类型
     * <p>输入1L long类型, 应该得到BigDecimal类型
     * <p>输入1.0 Double, 应该得到BigDecimal类型
     */
    @ParameterizedTest
    @CsvSource({"'1.0',1", "1,1", "1.0,1.0"})
    @DisplayName("测试宽表字段是BigDecimal类型, 输入其他类型数据和BigDecimal, 应该得到BigDecimal")
    void process6(Object inputParam, double expectValue) throws Exception {
        BigDecimal process = process(BigDecimal.class, inputParam);
        assertEquals(BigDecimal.valueOf(expectValue).compareTo(process), 0);
    }

    /**
     * 测试宽表字段是boolean, 输入其他类型数据和boolean
     * <p>输入"true"字符串, 应该返回true
     * <p>输入"false"字符串, 应该返回false
     * <p>输入"1"字符串, 应该返回true
     * <p>输入"0"字符串, 应该返回false
     * <p>输入1 int, 应该返回true
     * <p>输入0 int, 应该返回false
     * <p>输入true, 应该返回true
     * <p>输入false, 应该返回false
     */
    @ParameterizedTest
    @DisplayName("测试宽表字段是boolean类型, 输入其他类型数据和boolean, 应该得到boolean")
    @CsvSource({"'true',true", "'false',false", "'1',true", "'0',false", "1,true", "0,false", "true,true", "false,false"})
    void process7(Object inputParam, boolean expectValue) throws Exception {
        Boolean process = process(Boolean.class, inputParam);
        assertEquals(expectValue, process);
    }

    private <T> T process(Class<T> dataClass, Object inputParam) throws Exception {
        //初始化
        RealFieldCalculate<T> realFieldCalculate = new RealFieldCalculate<>();
        String fieldName = "key";
        realFieldCalculate.setColumnName(fieldName);
        realFieldCalculate.setDataClass(dataClass);
        realFieldCalculate.init();

        //构造数据
        Map<String, Object> input = new HashMap<>();
        input.put(fieldName, inputParam);
        //执行
        return realFieldCalculate.process(input);
    }

}