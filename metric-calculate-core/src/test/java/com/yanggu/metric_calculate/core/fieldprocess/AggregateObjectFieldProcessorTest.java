package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AggregateObjectFieldProcessorTest {

    @Test
    public void init() {
    }

    @Test
    public void process1() throws Exception {
        String amount = "amount";

        AggregateObjectFieldProcessor aggregateObjectFieldProcessor = new AggregateObjectFieldProcessor<>();
        aggregateObjectFieldProcessor.setAggregateType("MAXOBJECT");
        aggregateObjectFieldProcessor.setMetricExpress(amount);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        aggregateObjectFieldProcessor.setUnitFactory(unitFactory);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put(amount, Double.class);
        aggregateObjectFieldProcessor.setFieldMap(fieldMap);

        aggregateObjectFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set(amount, 100);
        input.set("name", "张三");

        MergedUnit process = aggregateObjectFieldProcessor.process(input);
        Object value = ValueMapper.value(((Value<?>) process));

        assertEquals(new HashMap<>(input), value);
    }

    @Test
    public void process2() throws Exception {

        AggregateObjectFieldProcessor<?> aggregateObjectFieldProcessor = new AggregateObjectFieldProcessor<>();
        aggregateObjectFieldProcessor.setAggregateType("OCCUPIEDOBJECT");

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        aggregateObjectFieldProcessor.setUnitFactory(unitFactory);

        Class<? extends MergedUnit<?>> mergeableClass = unitFactory
                .getMergeableClass(aggregateObjectFieldProcessor.getAggregateType());
        aggregateObjectFieldProcessor.setMergeUnitClazz(mergeableClass);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
        aggregateObjectFieldProcessor.setFieldMap(fieldMap);

        aggregateObjectFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = aggregateObjectFieldProcessor.process(input);
        Object value = ValueMapper.value(((Value<?>) process));

        assertEquals(new HashMap<>(input), value);
    }

    @Test
    public void process3() throws Exception {

        AggregateObjectFieldProcessor<?> aggregateObjectFieldProcessor = new AggregateObjectFieldProcessor<>();
        aggregateObjectFieldProcessor.setAggregateType("MAXOBJECT");
        aggregateObjectFieldProcessor.setMetricExpress("amount");

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        aggregateObjectFieldProcessor.setUnitFactory(unitFactory);

        Class<? extends MergedUnit<?>> mergeableClass = unitFactory
                .getMergeableClass(aggregateObjectFieldProcessor.getAggregateType());
        aggregateObjectFieldProcessor.setMergeUnitClazz(mergeableClass);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("name", String.class);
        aggregateObjectFieldProcessor.setFieldMap(fieldMap);

        Map<String, Object> udafParams = new HashMap<>();
        udafParams.put("onlyShowValue", true);
        aggregateObjectFieldProcessor.setUdafParams(udafParams);

        aggregateObjectFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("amount", 100);
        input.set("name", "张三");

        MergedUnit process = aggregateObjectFieldProcessor.process(input);
        Object value = ValueMapper.value(((Value<?>) process));

        assertEquals(input, value);
    }

}