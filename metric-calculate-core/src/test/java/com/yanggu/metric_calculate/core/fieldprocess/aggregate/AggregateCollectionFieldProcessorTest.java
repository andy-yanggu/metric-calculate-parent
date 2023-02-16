package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * AggregateCollectionFieldProcessor单元测试类
 * <p>聚合对集合型字段处理器单元测试类</p>
 */
public class AggregateCollectionFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    @Before
    public void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
        this.fieldMap = fieldMap;

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        this.unitFactory = unitFactory;
    }

    @Test
    public void testInit() {
    }

    /**
     * 测试ListFieldUnit, 集合型, 没有比较字段以及保留指定字段
     *
     * @throws Exception
     */
    @Test
    public void process1() throws Exception {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTFIELD");
        baseUdafParam.setRetainExpress("name");

        BaseAggregateFieldProcessor<?> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = collectionFieldProcessor.process(input);
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
     * @throws Exception
     */
    @Test
    public void process2() throws Exception {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("LISTOBJECT");
        BaseAggregateFieldProcessor<?> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = collectionFieldProcessor.process(input);
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
     * @throws Exception
     */
    @Test
    public void process4() throws Exception {

        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SORTEDLISTOBJECT");
        baseUdafParam.setCollectiveSortFieldList(Collections.singletonList(new FieldOrderParam("amount", false)));

        BaseAggregateFieldProcessor<?> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = collectionFieldProcessor.process(input);
        assertEquals(Collections.singletonList(new HashMap<>(input)), ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process.merge(collectionFieldProcessor.process(input2));
        assertEquals(Arrays.asList(new HashMap<>(input2), new HashMap<>(input)), ValueMapper.value((Value<?>) process));

        JSONObject input3 = new JSONObject();
        input3.set("amount", 50);
        input3.set("name", "张三3");
        process.merge(collectionFieldProcessor.process(input3));
        assertEquals(Arrays.asList(new HashMap<>(input2), new HashMap<>(input), new HashMap<>(input3)), ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试UniqueListField, 集合型, 有比较字段以及保留指定字段
     *
     * @throws Exception
     */
    @Test
    public void process6() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("DISTINCTLISTFIELD");
        baseUdafParam.setDistinctFieldList(Collections.singletonList("amount"));
        baseUdafParam.setRetainExpress("name");

        BaseAggregateFieldProcessor<?> collectionFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = collectionFieldProcessor.process(input);
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
