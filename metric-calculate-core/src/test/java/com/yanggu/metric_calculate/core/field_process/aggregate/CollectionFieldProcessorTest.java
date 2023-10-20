package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.UdafParamTestBase;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
class CollectionFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() {
        this.fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
    }

    @Test
    void testInit() {
    }

    /**
     * LISTFIELD: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    void process1() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("LISTFIELD", "name");
        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("name", "张三");
        String process = baseFieldProcessor.process(input);
        assertEquals("张三", process);

        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals("张三2", process);
    }

    /**
     * LISTOBJECT: 没有去重字段、没有排序字段和保留原始数据
     */
    @Test
    void process2() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("LISTOBJECT", null);
        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        JSONObject process = baseFieldProcessor.process(input);
        assertEquals(input, process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process = baseFieldProcessor.process(input2);
        assertEquals(input2, process);
    }

    /**
     * SORTEDLIMITLISTOBJECT: 有排序字段、没有去重字段和保留原始数据
     */
    @Test
    void process3() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLISTOBJECT", null, "amount");
        FieldProcessor<JSONObject, Pair<MultiFieldData, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
        assertEquals(input, process.getRight());
    }

    /**
     * SORTEDLIMITLISTFIELD: 有排序字段、没有去重字段和保留指定字段
     */
    @Test
    void process4() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLISTFIELD", "name", "amount");
        FieldProcessor<JSONObject, Pair<MultiFieldData, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, String> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
        assertEquals("张三", process.getRight());
    }

    /**
     * SORTEDLIMITLIST: 有排序字段、没有去重字段和不保留任何数据
     */
    @Test
    void process5() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SORTEDLIMITLIST", null, "amount");
        FieldProcessor<JSONObject, Pair<MultiFieldData, Void>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, Void> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
    }

    /**
     * DISTINCTLISTOBJECT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    void process6() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("DISTINCTLISTOBJECT", null, "amount");
        FieldProcessor<JSONObject, Pair<MultiFieldData, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getLeft().getFieldList().get(0));
        assertEquals(input, process.getRight());
    }

    /**
     * DISTINCTLISTFIELD: 没有排序字段、有去重字段和保留指定字段
     */
    @Test
    void process7() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("DISTINCTLISTFIELD", "name", "amount");
        FieldProcessor<JSONObject, Pair<MultiFieldData, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, String> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getLeft().getFieldList().get(0));
        assertEquals("张三", process.getRight());
    }

    /**
     * DISTINCTLIST: 没有排序字段、有去重字段和不保留任何数据
     */
    @Test
    void process8() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("DISTINCTLIST", null, "amount");
        FieldProcessor<JSONObject, MultiFieldData> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        MultiFieldData process = baseFieldProcessor.process(input);
        assertEquals(100, process.getFieldList().get(0));
    }

    /**
     * DISTINCTCOUNT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    void process9() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("DISTINCTCOUNT", null, "amount");
        FieldProcessor<JSONObject, MultiFieldData> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        MultiFieldData process = baseFieldProcessor.process(input);
        assertEquals(100, process.getFieldList().get(0));
    }

    /**
     * SLIDINGCOUNTWINDOW、滑动计数窗口函数: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    void process10() throws Exception {
        BaseUdafParam baseUdafParam = UdafParamTestBase.createBaseUdafParam("SLIDINGCOUNTWINDOW", "amount");
        FieldProcessor<JSONObject, Double> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100.0D);
        Double process = baseFieldProcessor.process(input);
        assertEquals(100.0D, process, 0.0D);

        input.set("amount", 200.0D);
        process = baseFieldProcessor.process(input);
        assertEquals(200.0D, process, 0.0D);
    }

}
