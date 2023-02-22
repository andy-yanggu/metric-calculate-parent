package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.DistinctListFieldUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListFieldUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.unit.collection.SortedListObjectUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getUnitFactory;
import static org.junit.Assert.assertEquals;

/**
 * AggregateCollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
public class AggregateCollectionFieldProcessorTest {

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
     * 测试ListFieldUnit, 集合型, 没有比较字段以及保留指定字段
     *
     */
    @Test
    public void process1() {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTFIELD");
        baseUdafParam.setRetainExpress("name");

        BaseAggregateFieldProcessor<JSONObject, ListFieldUnit<CloneWrapper<String>>> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<ListFieldUnit<CloneWrapper<String>>> process = collectionFieldProcessor.process(input);
        assertEquals(Collections.singletonList("张三"), ValueMapper.value(((Value<?>) process)));

        input.set("amount", 200);
        input.set("name", "张三2");
        process.merge(collectionFieldProcessor.process(input));
        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));

        input.set("amount", 100);
        input.set("name", "张三3");
        process.merge(collectionFieldProcessor.process(input));
        assertEquals(Arrays.asList("张三", "张三2", "张三3"), ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试ListObject, 集合型, 没有比较字段以及保留原始对象数据
     *
     */
    @Test
    public void process2() {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        BaseAggregateFieldProcessor<JSONObject, ListObjectUnit<CloneWrapper<JSONObject>>> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<ListObjectUnit<CloneWrapper<JSONObject>>> process = collectionFieldProcessor.process(input);
        assertEquals(Collections.singletonList(input), ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process.merge(collectionFieldProcessor.process(input2));
        assertEquals(Arrays.asList(input, input2), ValueMapper.value((Value<?>) process));

        JSONObject input3 = new JSONObject();
        input3.set("amount", 50);
        input3.set("name", "张三3");
        process.merge(collectionFieldProcessor.process(input3));
        assertEquals(Arrays.asList(input, input2, input3), ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试SortedListObject, 集合型, 需要比较字段以及保留原始对象数据
     *
     */
    @Test
    public void process4() {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLISTOBJECT");
        baseUdafParam.setCollectiveSortFieldList(Collections.singletonList(new FieldOrderParam("amount", false)));

        List<BaseUdafParam> baseUdafParamList = Collections.singletonList(baseUdafParam);
        BaseAggregateFieldProcessor<JSONObject, SortedListObjectUnit<KeyValue<MultiFieldOrderCompareKey, CloneWrapper<JSONObject>>>> collectionFieldProcessor =
                        FieldProcessorUtil.getBaseAggregateFieldProcessor(baseUdafParamList, getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        SortedListObjectUnit<KeyValue<MultiFieldOrderCompareKey, CloneWrapper<JSONObject>>> process = collectionFieldProcessor.process(input);
        assertEquals(Collections.singletonList(new HashMap<>(input)), ValueMapper.value(process));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process.merge(collectionFieldProcessor.process(input2));
        assertEquals(Arrays.asList(new HashMap<>(input2), new HashMap<>(input)), ValueMapper.value(process));

        JSONObject input3 = new JSONObject();
        input3.set("amount", 50);
        input3.set("name", "张三3");
        process.merge(collectionFieldProcessor.process(input3));
        assertEquals(Arrays.asList(new HashMap<>(input2), new HashMap<>(input), new HashMap<>(input3)), ValueMapper.value(process));
    }

    /**
     * 测试UniqueListField, 集合型, 有比较字段以及保留指定字段
     *
     */
    @Test
    public void process6() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("DISTINCTLISTFIELD");
        baseUdafParam.setDistinctFieldList(Collections.singletonList("amount"));
        baseUdafParam.setRetainExpress("name");

        BaseAggregateFieldProcessor<JSONObject, DistinctListFieldUnit<KeyValue<MultiFieldDistinctKey, CloneWrapper<String>>>> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<DistinctListFieldUnit<KeyValue<MultiFieldDistinctKey, CloneWrapper<String>>>> process =
                collectionFieldProcessor.process(input);
        assertEquals(Collections.singletonList("张三"), ValueMapper.value(((Value<?>) process)));

        input.set("amount", 200);
        input.set("name", "张三2");
        process.merge(collectionFieldProcessor.process(input));
        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));

        input.set("amount", 100);
        input.set("name", "张三3");
        process.merge(collectionFieldProcessor.process(input));
        assertEquals(Arrays.asList("张三", "张三2"), ValueMapper.value((Value<?>) process));
    }

}
