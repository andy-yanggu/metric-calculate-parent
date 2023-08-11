package com.yanggu.metric_calculate.core.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrder;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.util.KeyValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
class CollectionFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
        this.fieldMap = fieldMap;
    }

    @Test
    void testInit() {
    }

    /**
     * LISTFIELD: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    void process1() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        baseUdafParam.setRetainExpressParam(aviatorExpressParam);
        baseUdafParam.setAggregateType("LISTFIELD");

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

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
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
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLIMITLISTOBJECT");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setCollectiveSortFieldList(CollUtil.toList(new FieldOrderParam(aviatorExpressParam, false)));

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, JSONObject> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        FieldOrder fieldOrder = new FieldOrder();
        fieldOrder.setResult(100);
        fieldOrder.setAsc(false);
        multiFieldOrderCompareKey.setFieldOrderList(CollUtil.toList(fieldOrder));
        assertEquals(multiFieldOrderCompareKey, process.getKey());
        assertEquals(input, process.getValue());
    }

    /**
     * SORTEDLIMITLISTFIELD: 有排序字段、没有去重字段和保留指定字段
     */
    @Test
    void process4() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLIMITLISTFIELD");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setCollectiveSortFieldList(CollUtil.toList(new FieldOrderParam(aviatorExpressParam, false)));

        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("name");
        baseUdafParam.setRetainExpressParam(aviatorExpressParam1);

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldOrderCompareKey, String> process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        FieldOrder fieldOrder = new FieldOrder();
        fieldOrder.setResult(100);
        fieldOrder.setAsc(false);
        multiFieldOrderCompareKey.setFieldOrderList(CollUtil.toList(fieldOrder));
        assertEquals(multiFieldOrderCompareKey, process.getKey());
        assertEquals("张三", process.getValue());
    }

    /**
     * SORTEDLIMITLIST: 有排序字段、没有去重字段和不保留任何数据
     */
    @Test
    void process5() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLIMITLIST");
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setCollectiveSortFieldList(CollUtil.toList(new FieldOrderParam(aviatorExpressParam, false)));

        FieldProcessor<JSONObject, MultiFieldOrderCompareKey> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MultiFieldOrderCompareKey process = baseFieldProcessor.process(input);
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        FieldOrder fieldOrder = new FieldOrder();
        fieldOrder.setResult(100);
        fieldOrder.setAsc(false);
        multiFieldOrderCompareKey.setFieldOrderList(CollUtil.toList(fieldOrder));
        assertEquals(multiFieldOrderCompareKey, process);
    }

    /**
     * DISTINCTLISTOBJECT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    void process6() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setDistinctFieldListParamList(CollUtil.toList(aviatorExpressParam));
        baseUdafParam.setAggregateType("DISTINCTLISTOBJECT");

        FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, JSONObject>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldDistinctKey, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getKey().getFieldList().get(0));
        assertEquals(input, process.getValue());
    }

    /**
     * DISTINCTLISTFIELD: 没有排序字段、有去重字段和保留指定字段
     */
    @Test
    void process7() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setDistinctFieldListParamList(CollUtil.toList(aviatorExpressParam));
        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("name");
        baseUdafParam.setRetainExpressParam(aviatorExpressParam2);
        baseUdafParam.setAggregateType("DISTINCTLISTFIELD");

        FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, String>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldDistinctKey, String> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getKey().getFieldList().get(0));
        assertEquals("张三", process.getValue());
    }

    /**
     * DISTINCTLIST: 没有排序字段、有去重字段和不保留任何数据
     */
    @Test
    void process8() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setDistinctFieldListParamList(CollUtil.toList(aviatorExpressParam));
        baseUdafParam.setAggregateType("DISTINCTLIST");

        FieldProcessor<JSONObject, MultiFieldDistinctKey> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MultiFieldDistinctKey process = baseFieldProcessor.process(input);
        assertEquals(100, process.getFieldList().get(0));
    }

    /**
     * DISTINCTCOUNT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    void process9() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setDistinctFieldListParamList(CollUtil.toList(aviatorExpressParam));
        baseUdafParam.setAggregateType("DISTINCTCOUNT");

        FieldProcessor<JSONObject, MultiFieldDistinctKey> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MultiFieldDistinctKey process = baseFieldProcessor.process(input);
        assertEquals(100, process.getFieldList().get(0));
    }

    /**
     * SLIDINGCOUNTWINDOW、滑动计数窗口函数: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    void process10() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setRetainExpressParam(aviatorExpressParam);
        baseUdafParam.setAggregateType("SLIDINGCOUNTWINDOW");

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
