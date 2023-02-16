package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.pojo.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * AggregateObjectFieldProcessor单元测试类
 * <p>聚合对象型字段处理器单元测试类</p>
 */
public class AggregateObjectFieldProcessorTest {

    @Test
    public void init() {
    }

    /**
     * 测试MAXFIELD, 对象型, 有比较字段以及保留指定字段
     *
     * @throws Exception
     */
    @Test
    public void process1() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXFIELD");
        udafParam.setRetainExpress("name");
        //金额作为比较字段
        udafParam.setObjectiveCompareFieldList(Collections.singletonList("amount"));

        BaseAggregateFieldProcessor<?> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
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
     *
     * @throws Exception
     */
    @Test
    public void process2() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("MAXOBJECT");
        udafParam.setObjectiveCompareFieldList(Collections.singletonList("amount"));

        BaseAggregateFieldProcessor<?> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
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
     *
     * @throws Exception
     */
    @Test
    public void process5() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setRetainExpress("name");
        udafParam.setAggregateType("OCCUPIEDFIELD");

        BaseAggregateFieldProcessor<?> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
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
     * @throws Exception
     */
    @Test
    public void process6() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        BaseUdafParam udafParam = new BaseUdafParam();
        udafParam.setAggregateType("OCCUPIEDOBJECT");

        BaseAggregateFieldProcessor<?> objectFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(udafParam), unitFactory, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
        assertEquals(new HashMap<>(input), ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process.merge(objectFieldProcessor.process(input2));
        assertEquals(new HashMap<>(input), ValueMapper.value((Value<?>) process));
    }

}