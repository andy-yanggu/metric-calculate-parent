package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrder;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactoryBase.getAggregateFunctionFactory;
import static org.junit.Assert.assertEquals;

/**
 * CollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
public class CollectionFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @Before
    public void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
        this.fieldMap = fieldMap;
    }

    @Test
    public void testInit() {
    }

    /**
     * LISTFIELD: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    public void process1() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setRetainExpress("name");
        baseUdafParam.setAggregateType("LISTFIELD");

        FieldProcessor<JSONObject, String> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
    public void process2() throws Exception {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        FieldProcessor<JSONObject, JSONObject> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
    public void process3() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLIMITLISTOBJECT");
        baseUdafParam.setCollectiveSortFieldList(CollUtil.toList(new FieldOrderParam("amount", false)));

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, JSONObject>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
    public void process4() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLIMITLISTFIELD");
        baseUdafParam.setCollectiveSortFieldList(CollUtil.toList(new FieldOrderParam("amount", false)));
        baseUdafParam.setRetainExpress("name");

        FieldProcessor<JSONObject, KeyValue<MultiFieldOrderCompareKey, String>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
     * DISTINCTLISTOBJECT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    public void process5() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setDistinctFieldList(CollUtil.toList("amount"));
        baseUdafParam.setAggregateType("DISTINCTLISTOBJECT");

        FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, JSONObject>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
    public void process6() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setDistinctFieldList(CollUtil.toList("amount"));
        baseUdafParam.setRetainExpress("name");
        baseUdafParam.setAggregateType("DISTINCTLISTFIELD");

        FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, String>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldDistinctKey, String> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getKey().getFieldList().get(0));
        assertEquals("张三", process.getValue());
    }

    /**
     * DISTINCTCOUNT: 没有排序字段、有去重字段和保留原始数据
     */
    @Test
    public void process7() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setDistinctFieldList(CollUtil.toList("amount"));
        baseUdafParam.setAggregateType("DISTINCTCOUNT");

        FieldProcessor<JSONObject, KeyValue<MultiFieldDistinctKey, JSONObject>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        KeyValue<MultiFieldDistinctKey, JSONObject> process = baseFieldProcessor.process(input);
        assertEquals(100, process.getKey().getFieldList().get(0));
        assertEquals(input, process.getValue());
    }

    /**
     * SLIDINGCOUNTWINDOW、滑动计数窗口函数: 没有去重字段、没有排序字段和保留指定字段
     */
    @Test
    public void process8() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setRetainExpress("amount");
        baseUdafParam.setAggregateType("SLIDINGCOUNTWINDOW");

        FieldProcessor<JSONObject, Double> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
