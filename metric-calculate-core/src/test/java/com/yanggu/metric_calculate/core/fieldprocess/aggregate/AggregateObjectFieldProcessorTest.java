package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.object.MaxFieldUnit;
import com.yanggu.metric_calculate.core.unit.object.MaxObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.OccupiedFieldUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getUnitFactory;
import static org.junit.Assert.assertEquals;

/**
 * AggregateObjectFieldProcessor单元测试类
 * <p>聚合对象型字段处理器单元测试类</p>
 */
public class AggregateObjectFieldProcessorTest {

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
     * 测试MAXFIELD, 对象型, 有比较字段以及保留指定字段
     */
    @Test
    public void process1() {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXFIELD");
        udafParam.setRetainExpress("name");
        //金额作为比较字段
        udafParam.setObjectiveCompareFieldList(Collections.singletonList("amount"));

        BaseAggregateFieldProcessor<JSONObject, MaxFieldUnit<MultiFieldOrderCompareKey>> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<MaxFieldUnit<MultiFieldOrderCompareKey>> process = objectFieldProcessor.process(input);
        assertEquals("张三", ValueMapper.value(((Value<?>) process)));

        input.set("amount", 200);
        input.set("name", "张三2");
        process.merge(objectFieldProcessor.process(input));
        assertEquals("张三2", ValueMapper.value((Value<?>) process));

        input.set("amount", 50);
        input.set("name", "张三3");
        process.merge(objectFieldProcessor.process(input));
        assertEquals("张三2", ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试MaxObject, 对象型, 需要比较字段以及保留原始对象数据
     */
    @Test
    public void process2() {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXOBJECT");
        udafParam.setObjectiveCompareFieldList(Collections.singletonList("amount"));

        BaseAggregateFieldProcessor<JSONObject, MaxObjectUnit<MultiFieldOrderCompareKey>> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<MaxObjectUnit<MultiFieldOrderCompareKey>> process = objectFieldProcessor.process(input);
        assertEquals(new HashMap<>(input), ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("name", "张三");
        input2.set("amount", 200);
        process.merge(objectFieldProcessor.process(input2));
        assertEquals(new HashMap<>(input2), ValueMapper.value((Value<?>) process));

        JSONObject input3 = new JSONObject();
        input3.set("name", "张三");
        input3.set("amount", 100);
        process.merge(objectFieldProcessor.process(input3));
        assertEquals(new HashMap<>(input2), ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试OccupiedFieldUnit, 对象型, 没有比较字段以及保留指定字段
     */
    @Test
    public void process5() {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setRetainExpress("name");
        udafParam.setAggregateType("OCCUPIEDFIELD");

        BaseAggregateFieldProcessor<JSONObject, OccupiedFieldUnit<Cloneable2Wrapper<String>>> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<OccupiedFieldUnit<Cloneable2Wrapper<String>>> process = objectFieldProcessor.process(input);
        assertEquals("张三", ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200);
        input2.set("name", "张三2");
        process.merge(objectFieldProcessor.process(input2));
        assertEquals("张三", ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试OCCUPIEDOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     *
     */
    @Test
    public void process6() {

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("OCCUPIEDOBJECT");

        BaseAggregateFieldProcessor<JSONObject, OccupiedFieldUnit<Cloneable2Wrapper<JSONObject>>> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), getUnitFactory(), fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit<OccupiedFieldUnit<Cloneable2Wrapper<JSONObject>>> process = objectFieldProcessor.process(input);
        assertEquals(new HashMap<>(input), ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process.merge(objectFieldProcessor.process(input2));
        assertEquals(new HashMap<>(input), ValueMapper.value((Value<?>) process));
    }

}