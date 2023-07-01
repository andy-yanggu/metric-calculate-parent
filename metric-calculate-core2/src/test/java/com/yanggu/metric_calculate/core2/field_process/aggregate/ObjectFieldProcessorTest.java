package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrder;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.field_process.aggregate.FieldProcessorTestBase.getBaseFieldProcessor;
import static org.junit.Assert.assertEquals;

/**
 * ObjectFieldProcessor单元测试类
 * <p>聚合对象型字段处理器单元测试类</p>
 */
public class ObjectFieldProcessorTest {

    private final Map<String, Class<?>> fieldMap = new HashMap<>();

    @Before
    public void before() {
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
    }

    @Test
    public void init() {
    }

    /**
     * 测试FIRSTFIELD, 对象型, 没有比较字段以及保留指定字段
     */
    @Test
    public void process1() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setRetainExpressParam(aviatorExpressParam);
        udafParam.setAggregateType("FIRSTFIELD");

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process2() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("FIRSTOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process3() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setRetainExpressParam(aviatorExpressParam);
        udafParam.setAggregateType("LAGFIELD");

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process4() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LAGOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process5() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setRetainExpressParam(aviatorExpressParam);
        udafParam.setAggregateType("LASTFIELD");

        FieldProcessor<JSONObject, String> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process6() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("LASTOBJECT");

        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

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
    public void process7() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setRetainExpressParam(aviatorExpressParam);
        udafParam.setAggregateType("MAXFIELD");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, String>> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, String> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals("张三", process.getValue());
        assertEquals(compareKey, process.getKey());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals("张三2", process.getValue());
        assertEquals(compareKey, process.getKey());
    }

    /**
     * 测试MAXOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @Test
    public void process8() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));
        udafParam.setAggregateType("MAXOBJECT");

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, JSONObject>> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, JSONObject> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals(compareKey, process.getKey());
        assertEquals(input, process.getValue());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals(compareKey, process.getKey());
        assertEquals(input, process.getValue());
    }

    /**
     * MAXVALUE: 有比较字段和不保留任何数据
     */
    @Test
    public void process9() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXVALUE");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));

        FieldProcessor<JSONObject, MultiFieldOrderCompareKey> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MultiFieldOrderCompareKey process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals(compareKey, process);

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals(compareKey, process);
    }

    /**
     * MINFIELD: 有比较字段以及保留指定字段
     */
    @Test
    public void process10() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        udafParam.setRetainExpressParam(aviatorExpressParam);
        udafParam.setAggregateType("MINFIELD");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, String>> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, String> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals("张三", process.getValue());
        assertEquals(compareKey, process.getKey());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals("张三2", process.getValue());
        assertEquals(compareKey, process.getKey());
    }

    /**
     * 测试MINOBJECT, 对象型, 需要比较字段以及保留原始对象数据
     */
    @Test
    public void process11() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));
        udafParam.setAggregateType("MINOBJECT");

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, JSONObject>> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, JSONObject> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals(compareKey, process.getKey());
        assertEquals(input, process.getValue());

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals(compareKey, process.getKey());
        assertEquals(input, process.getValue());
    }

    /**
     * MINVALUE: 有比较字段和不保留任何数据
     */
    @Test
    public void process12() throws Exception {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MINVALUE");
        //金额作为比较字段
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");
        udafParam.setObjectiveCompareFieldParamList(Collections.singletonList(aviatorExpressParam1));

        FieldProcessor<JSONObject, MultiFieldOrderCompareKey> baseFieldProcessor = getBaseFieldProcessor(fieldMap, udafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MultiFieldOrderCompareKey process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey compareKey = new MultiFieldOrderCompareKey();
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(100)));
        assertEquals(compareKey, process);

        input.set("amount", 200);
        input.set("name", "张三2");
        process = baseFieldProcessor.process(input);
        compareKey.setFieldOrderList(CollUtil.toList(new FieldOrder().setAsc(true).setResult(200)));
        assertEquals(compareKey, process);
    }

}