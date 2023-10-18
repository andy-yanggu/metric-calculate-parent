package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import org.dromara.hutool.core.lang.tuple.Pair;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ObjectFieldProcessor单元测试类
 * <p>聚合对象型字段处理器单元测试类</p>
 */
class ObjectFieldProcessorTest {

    private final Map<String, Class<?>> fieldMap = new HashMap<>();

    @BeforeEach
    void before() {
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
    }

    @Test
    void init() {
    }

    /**
     * 测试FIRSTFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @Test
    void process1() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("FIRSTFIELD");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setMetricExpressParam(aviatorExpressParam);

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        String process = baseFieldProcessor.process(input);
        assertEquals("张三", process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process = baseFieldProcessor.process(input2);
        assertEquals("张三2", process);
    }

    /**
     * 测试FIRSTOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @Test
    void process2() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("FIRSTOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        JSONObject process = baseFieldProcessor.process(input);
        assertEquals(input, process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process = baseFieldProcessor.process(input2);
        assertEquals(input2, process);
    }

    /**
     * 测试LAGFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @Test
    void process3() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LAGFIELD");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setMetricExpressParam(aviatorExpressParam);

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        String process = baseFieldProcessor.process(input);
        assertEquals("张三", process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process = baseFieldProcessor.process(input2);
        assertEquals("张三2", process);
    }

    /**
     * 测试LAGOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @Test
    void process4() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LAGOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        JSONObject process = baseFieldProcessor.process(input);
        assertEquals(input, process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process = baseFieldProcessor.process(input2);
        assertEquals(input2, process);
    }

    /**
     * 测试LASTFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @Test
    void process5() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LASTFIELD");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setMetricExpressParam(aviatorExpressParam);

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        String process = baseFieldProcessor.process(input);
        assertEquals("张三", process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process = baseFieldProcessor.process(input2);
        assertEquals("张三2", process);
    }

    /**
     * 测试LASTOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     */
    @Test
    void process6() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LASTOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        JSONObject process = baseFieldProcessor.process(input);
        assertEquals(input, process);

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process = baseFieldProcessor.process(input2);
        assertEquals(input2, process);
    }

    /**
     * MAXFIELD: 有比较字段以及保留指定字段
     */
    @Test
    void process7() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXFIELD");
        //name作为保留字段
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setMetricExpressParam(aviatorExpressParam);
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        Pair<MultiFieldData, String> process = baseFieldProcessor.process(input);
        assertEquals("张三", process.getRight());
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals("张三2", process.getRight());
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
    }

    /**
     * 测试MAXOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @Test
    void process8() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXOBJECT");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
        assertEquals(input, process.getRight());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
        assertEquals(input, process.getRight());
    }

    /**
     * MAXVALUE: 有比较字段和不保留任何数据
     */
    @Test
    void process9() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXVALUE");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, Void>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        Pair<MultiFieldData, Void> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
    }

    /**
     * MINFIELD: 有比较字段以及保留指定字段
     */
    @Test
    void process10() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MINFIELD");
        //name作为保留字段
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setMetricExpressParam(aviatorExpressParam);
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        Pair<MultiFieldData, String> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
        assertEquals("张三", process.getRight());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
        assertEquals("张三2", process.getRight());
    }

    /**
     * 测试MINOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @Test
    void process11() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MINOBJECT");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());
        assertEquals(input, process.getRight());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
        assertEquals(input, process.getRight());
    }

    /**
     * MINVALUE: 有比较字段和不保留任何数据
     */
    @Test
    void process12() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MINVALUE");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setMetricExpressParamList(List.of(aviatorExpressParam1));

        FieldProcessor<JSONObject, Pair<MultiFieldData, Void>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");
        Pair<MultiFieldData, Void> process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(100)), process.getLeft());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        assertEquals(new MultiFieldData(List.of(200)), process.getLeft());
    }

}