package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.UdafParamTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ObjectFieldProcessor单元测试类
 * <p>聚合对象型字段处理器单元测试类</p>
 */
@DisplayName("ObjectFieldProcessor单元测试类")
class ObjectFieldProcessorTest {

    private static Map<String, FieldProcessor<Map<String, Object>, ?>> fieldProcessorMap;

    @BeforeAll
    static void init() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        fieldProcessorMap = new HashMap<>();
        BaseUdafParam udafParam1 = UdafParamTestBase.createBaseUdafParam("FIRSTFIELD", "name");
        FieldProcessor<Map<String, Object>, String> firstFieldObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam1);
        fieldProcessorMap.put("FIRSTFIELD", firstFieldObjectFieldProcessor);

        BaseUdafParam udafParam2 = UdafParamTestBase.createBaseUdafParam("FIRSTOBJECT", null);
        FieldProcessor<Map<String, Object>, Map<String, Object>> firstObjectObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam2);
        fieldProcessorMap.put("FIRSTOBJECT", firstObjectObjectFieldProcessor);

        BaseUdafParam udafParam3 = UdafParamTestBase.createBaseUdafParam("LAGFIELD", "name");
        FieldProcessor<Map<String, Object>, String> lagFieldObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam3);
        fieldProcessorMap.put("LAGFIELD", lagFieldObjectFieldProcessor);

        BaseUdafParam udafParam4 = UdafParamTestBase.createBaseUdafParam("LAGOBJECT", null);
        FieldProcessor<Map<String, Object>, Map<String, Object>> lagObjectObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam4);
        fieldProcessorMap.put("LAGOBJECT", lagObjectObjectFieldProcessor);

        BaseUdafParam udafParam5 = UdafParamTestBase.createBaseUdafParam("LASTFIELD", "name");
        FieldProcessor<Map<String, Object>, String> lastFieldObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam5);
        fieldProcessorMap.put("LASTFIELD", lastFieldObjectFieldProcessor);

        BaseUdafParam udafParam6 = UdafParamTestBase.createBaseUdafParam("LASTOBJECT", null);
        FieldProcessor<Map<String, Object>, Map<String, Object>> lastObjectObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam6);
        fieldProcessorMap.put("LASTOBJECT", lastObjectObjectFieldProcessor);

        BaseUdafParam udafParam7 = UdafParamTestBase.createBaseUdafParam("MAXFIELD", "name", "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, String>> maxFieldObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam7);
        fieldProcessorMap.put("MAXFIELD", maxFieldObjectFieldProcessor);

        BaseUdafParam udafParam8 = UdafParamTestBase.createBaseUdafParam("MAXOBJECT", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Map<String, Object>>> maxObjectObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam8);
        fieldProcessorMap.put("MAXOBJECT", maxObjectObjectFieldProcessor);

        BaseUdafParam udafParam9 = UdafParamTestBase.createBaseUdafParam("MAXVALUE", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Void>> maxValueObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam9);
        fieldProcessorMap.put("MAXVALUE", maxValueObjectFieldProcessor);

        BaseUdafParam udafParam10 = UdafParamTestBase.createBaseUdafParam("MINFIELD", "name", "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, String>> minFieldObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam10);
        fieldProcessorMap.put("MINFIELD", minFieldObjectFieldProcessor);

        BaseUdafParam udafParam11 = UdafParamTestBase.createBaseUdafParam("MINOBJECT", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Map<String, Object>>> minObjectObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam11);
        fieldProcessorMap.put("MINOBJECT", minObjectObjectFieldProcessor);

        BaseUdafParam udafParam12 = UdafParamTestBase.createBaseUdafParam("MINVALUE", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Void>> minValueObjectFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam12);
        fieldProcessorMap.put("MINVALUE", minValueObjectFieldProcessor);
    }

    @Test
    void testInit() {
    }

    /**
     * 测试FIRSTFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @ParameterizedTest
    @DisplayName("测试FIRSTFIELD, 对象型, 没有比较字段以及保留指定字段")
    @CsvSource({"100,'张三'", "200,'张三2'"})
    void process1(Double amount, String name) throws Exception {
        String process = process("FIRSTFIELD", amount, name);
        assertEquals(name, process);
    }

    /**
     * 测试FIRSTOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试FIRSTOBJECT, 对象型, 没有比较字段以及保留原始对象数据")
    void process2(Double amount, String name) throws Exception {
        Map<String, Object> process = process("FIRSTOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(buildParamMap(amount, name), process);
    }

    /**
     * 测试LAGFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试LAGFIELD, 对象型, 没有比较字段以及保留指定字段")
    void process3(Double amount, String name) throws Exception {
        String process = process("LAGFIELD", amount, name);
        assertEquals(name, process);
    }

    /**
     * 测试LAGOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试LAGOBJECT, 对象型, 没有比较字段以及保留原始对象数据")
    void process4(Double amount, String name) throws Exception {
        Map<String, Object> process = process("LAGOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(buildParamMap(amount, name), process);
    }

    /**
     * 测试LASTFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试LASTFIELD, 对象型, 没有比较字段以及保留指定字段")
    void process5(Double amount, String name) throws Exception {
        String process = process("LASTFIELD", amount, name);
        assertEquals(name, process);
    }

    /**
     * 测试LASTOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试LASTOBJECT, 对象型, 没有比较字段以及保留原始对象数据")
    void process6(Double amount, String name) throws Exception {
        Map<String, Object> process = process("LASTOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(buildParamMap(amount, name), process);
    }

    /**
     * 测试MAXFIELD: 有比较字段以及保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MAXFIELD: 有比较字段以及保留指定字段")
    void process7(Double amount, String name) throws Exception {
        Pair<MultiFieldData, String> process = process("MAXFIELD", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(name, process.getRight());
    }

    /**
     * 测试MAXOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MAXOBJECT, 对象型, 需要比较字段以及保留原始对象数据")
    void process8(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Map<String, Object>> process = process("MAXOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(buildParamMap(amount, name), process.getRight());
    }

    /**
     * 测试MAXVALUE: 有比较字段和不保留任何数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MAXVALUE: 有比较字段和不保留任何数据")
    void process9(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Void> process = process("MAXVALUE", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertNull(process.getRight());
    }

    /**
     * 测试MINFIELD: 有比较字段以及保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MINFIELD: 有比较字段以及保留指定字段")
    void process10(Double amount, String name) throws Exception {
        Pair<MultiFieldData, String> process = process("MINFIELD", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(name, process.getRight());
    }

    /**
     * 测试MINOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MINOBJECT, 对象型, 需要比较字段以及保留原始对象数据")
    void process11(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Map<String, Object>> process = process("MINOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(buildParamMap(amount, name), process.getRight());
    }

    /**
     * 测试MINVALUE: 有比较字段和不保留任何数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("测试MINVALUE: 有比较字段和不保留任何数据")
    void process12(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Void> process = process("MINVALUE", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertNull(process.getRight());
    }

    private Map<String, Object> buildParamMap(Double amount, String name) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("amount", amount);
        paramMap.put("name", name);
        return paramMap;
    }

    private <T> T process(String aggregateType, Double amount, String name) throws Exception {
        //构造参数
        Map<String, Object> paramMap = buildParamMap(amount, name);

        FieldProcessor<Map<String, Object>, ?> fieldProcessor = fieldProcessorMap.get(aggregateType);
        assertNotNull(fieldProcessor, "aggregateType错误");

        return (T) fieldProcessor.process(paramMap);
    }

}