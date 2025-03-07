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
 * CollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
@DisplayName("CollectionFieldProcessor单元测试类")
class CollectionFieldProcessorTest {

    private static Map<String, FieldProcessor<Map<String, Object>, ?>> fieldProcessorMap;

    @BeforeAll
    static void init() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        fieldProcessorMap = new HashMap<>();
        BaseUdafParam baseUdafParam1 = UdafParamTestBase.createBaseUdafParam("LISTFIELD", "name");
        FieldProcessor<Map<String, Object>, String> listFieldCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam1);
        fieldProcessorMap.put("LISTFIELD", listFieldCollectionFieldProcessor);

        BaseUdafParam baseUdafParam2 = UdafParamTestBase.createBaseUdafParam("LISTOBJECT", null);
        FieldProcessor<Map<String, Object>, Map<String, Object>> listObjectCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam2);
        fieldProcessorMap.put("LISTOBJECT", listObjectCollectionFieldProcessor);

        BaseUdafParam baseUdafParam3 = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLISTOBJECT", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Map<String, Object>>> sortListObjectCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam3);
        fieldProcessorMap.put("SORTEDLIMITLISTOBJECT", sortListObjectCollectionFieldProcessor);

        BaseUdafParam baseUdafParam4 = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLISTFIELD", "name", "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, String>> sortListFieldCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam4);
        fieldProcessorMap.put("SORTEDLIMITLISTFIELD", sortListFieldCollectionFieldProcessor);

        BaseUdafParam baseUdafParam5 = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLIST", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Void>> sortListCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam5);
        fieldProcessorMap.put("SORTEDLIMITLIST", sortListCollectionFieldProcessor);

        BaseUdafParam baseUdafParam6 = UdafParamTestBase.createBaseUdafParam("DISTINCTLISTOBJECT", null, "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, Map<String, Object>>> distinctListObjectCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam6);
        fieldProcessorMap.put("DISTINCTLISTOBJECT", distinctListObjectCollectionFieldProcessor);

        BaseUdafParam baseUdafParam7 = UdafParamTestBase.createBaseUdafParam("DISTINCTLISTFIELD", "name", "amount");
        FieldProcessor<Map<String, Object>, Pair<MultiFieldData, String>> distinctListFieldCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam7);
        fieldProcessorMap.put("DISTINCTLISTFIELD", distinctListFieldCollectionFieldProcessor);

        BaseUdafParam baseUdafParam8 = UdafParamTestBase.createBaseUdafParam("DISTINCTLIST", null, "amount");
        FieldProcessor<Map<String, Object>, MultiFieldData> distinctListCollectionFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam8);
        fieldProcessorMap.put("DISTINCTLIST", distinctListCollectionFieldProcessor);

        BaseUdafParam baseUdafParam9 = UdafParamTestBase.createBaseUdafParam("DISTINCTCOUNT", null, "amount");
        FieldProcessor<Map<String, Object>, MultiFieldData> distinctCountFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam9);
        fieldProcessorMap.put("DISTINCTCOUNT", distinctCountFieldProcessor);
    }

    @Test
    void testInit() {
    }

    /**
     * LISTFIELD: 没有去重字段、没有排序字段和保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("LISTFIELD: 没有去重字段、没有排序字段和保留指定字段")
    void process1(Double amount, String name) throws Exception {
        String process = process("LISTFIELD", amount, name);
        assertEquals(name, process);
    }

    /**
     * LISTOBJECT: 没有去重字段、没有排序字段和保留原始数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("LISTOBJECT: 没有去重字段、没有排序字段和保留原始数据")
    void process(Double amount, String name) throws Exception {
        Map<String, Object> process = process("LISTOBJECT", amount, name);
        assertEquals(buildParamMap(amount, name), process);
    }

    /**
     * SORTEDLIMITLISTOBJECT: 有排序字段、没有去重字段和保留原始数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("SORTEDLIMITLISTOBJECT: 有排序字段、没有去重字段和保留原始数据")
    void process3(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Map<String, Object>> process = process("SORTEDLIMITLISTOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(buildParamMap(amount, name), process.getRight());
    }

    /**
     * SORTEDLIMITLISTFIELD: 有排序字段、没有去重字段和保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("SORTEDLIMITLISTFIELD: 有排序字段、没有去重字段和保留指定字段")
    void process4(Double amount, String name) throws Exception {
        Pair<MultiFieldData, String> process = process("SORTEDLIMITLISTFIELD", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(name, process.getRight());
    }

    /**
     * SORTEDLIMITLIST: 有排序字段、没有去重字段和不保留任何数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("SORTEDLIMITLIST: 有排序字段、没有去重字段和不保留任何数据")
    void process5(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Void> process = process("SORTEDLIMITLIST", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertNull(process.getRight());
    }

    /**
     * DISTINCTLISTOBJECT: 没有排序字段、有去重字段和保留原始数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("DISTINCTLISTOBJECT: 没有排序字段、有去重字段和保留原始数据")
    void process6(Double amount, String name) throws Exception {
        Pair<MultiFieldData, Map<String, Object>> process = process("DISTINCTLISTOBJECT", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(buildParamMap(amount, name), process.getRight());
    }

    /**
     * DISTINCTLISTFIELD: 没有排序字段、有去重字段和保留指定字段
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("DISTINCTLISTFIELD: 没有排序字段、有去重字段和保留指定字段")
    void process7(Double amount, String name) throws Exception {
        Pair<MultiFieldData, String> process = process("DISTINCTLISTFIELD", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getLeft().getFieldList());
        assertEquals(name, process.getRight());
    }

    /**
     * DISTINCTLIST: 没有排序字段、有去重字段和不保留任何数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("DISTINCTLIST: 没有排序字段、有去重字段和不保留任何数据")
    void process8(Double amount, String name) throws Exception {
        MultiFieldData process = process("DISTINCTLIST", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getFieldList());
    }

    /**
     * DISTINCTCOUNT: 没有排序字段、有去重字段和保留原始数据
     */
    @ParameterizedTest
    @CsvSource({"100,'张三'", "200,'张三2'"})
    @DisplayName("DISTINCTCOUNT: 没有排序字段、有去重字段和保留原始数据")
    void process9(Double amount, String name) throws Exception {
        MultiFieldData process = process("DISTINCTCOUNT", amount, name);

        assertNotNull(process);
        assertEquals(List.of(amount), process.getFieldList());
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
