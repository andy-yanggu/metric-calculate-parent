package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.object.MaxFieldUnit;
import com.yanggu.metric_calculate.core.unit.object.MaxObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.OccupiedFieldUnit;
import com.yanggu.metric_calculate.core.unit.object.OccupiedObjectUnit;
import com.yanggu.metric_calculate.core.value.*;
import org.junit.Test;

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
     * 测试MaxObject, 对象型, 需要比较字段以及保留原始对象数据
     *
     * @throws Exception
     */
    @Test
    public void process1() throws Exception {

        //构造对象型字段处理器
        String compareField = "amount";
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(compareField, Double.class);
        fieldMap.put("name", String.class);

        AggregateObjectFieldProcessor<?> objectFieldProcessor =
                getObjectFieldProcessor(MaxObjectUnit.class, compareField, null, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set(compareField, 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
        assertEquals(input, ValueMapper.value(((Value<?>) process)));

        input.set(compareField, 200);
        process.merge(objectFieldProcessor.process(input));
        assertEquals(input, ValueMapper.value((Value<?>) process));

        input.set(compareField, 100);
        process.merge(objectFieldProcessor.process(input));
        assertEquals(input.set(compareField, 100), ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试OCCUPIEDOBJECT, 对象型, 没有比较字段以及保留原始对象数据
     *
     * @throws Exception
     */
    @Test
    public void process2() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        AggregateObjectFieldProcessor<?> objectFieldProcessor =
                getObjectFieldProcessor(OccupiedObjectUnit.class, null, null, fieldMap);

        //构造原始数据
        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = objectFieldProcessor.process(input);
        assertEquals(input, ValueMapper.value(((Value<?>) process)));

        JSONObject input2 = new JSONObject();
        input2.set("amount", 100);
        input2.set("name", "张三");
        process.merge(objectFieldProcessor.process(input2));
        assertEquals(input, ValueMapper.value((Value<?>) process));
    }

    /**
     * 测试MAXFIELD, 对象型, 有比较字段以及保留指定字段
     *
     * @throws Exception
     */
    @Test
    public void process3() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        AggregateObjectFieldProcessor<?> objectFieldProcessor =
                getObjectFieldProcessor(MaxFieldUnit.class, "amount", "name", fieldMap);

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
     * 测试OccupiedFieldUnit, 对象型, 没有比较字段以及保留指定字段
     *
     * @throws Exception
     */
    @Test
    public void process4() throws Exception {

        //构造对象型字段处理器
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);

        AggregateObjectFieldProcessor<?> objectFieldProcessor =
                getObjectFieldProcessor(OccupiedFieldUnit.class, null, "name", fieldMap);

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

    private AggregateObjectFieldProcessor<?> getObjectFieldProcessor(Class<?> clazz,
                                                                     String compareField,
                                                                     String retainField,
                                                                     Map<String, Class<?>> fieldMap) throws Exception {
        String maxObject = clazz.getAnnotation(MergeType.class).value();

        AggregateObjectFieldProcessor<?> aggregateObjectFieldProcessor = new AggregateObjectFieldProcessor<>();
        aggregateObjectFieldProcessor.setAggregateType(maxObject);
        aggregateObjectFieldProcessor.setMetricExpress(compareField);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        aggregateObjectFieldProcessor.setUnitFactory(unitFactory);

        Class<? extends MergedUnit<?>> mergeableClass = unitFactory.getMergeableClass(maxObject);
        aggregateObjectFieldProcessor.setMergeUnitClazz(mergeableClass);

        aggregateObjectFieldProcessor.setFieldMap(fieldMap);

        if (StrUtil.isNotBlank(retainField)) {
            MetricFieldProcessor<Object> retainFieldProcessor = new MetricFieldProcessor<>();
            retainFieldProcessor.setMetricExpress(retainField);
            retainFieldProcessor.setFieldMap(fieldMap);
            retainFieldProcessor.init();
            aggregateObjectFieldProcessor.setRetainFieldValueFieldProcessor(retainFieldProcessor);
        }

        //执行初始化操作
        aggregateObjectFieldProcessor.init();
        return aggregateObjectFieldProcessor;
    }

}