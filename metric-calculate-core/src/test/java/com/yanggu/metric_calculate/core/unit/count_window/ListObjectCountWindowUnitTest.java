package com.yanggu.metric_calculate.core.unit.count_window;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.BaseAggregateFieldProcessor;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.count_window.ListObjectCountWindowUnit.Fields;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * 计数滑动窗口测试类
 */
public class ListObjectCountWindowUnitTest {

    @Test
    public void value() throws Exception {

        //最近5条数据的求和
        Map<String, Object> param = new HashMap<>();
        param.put(Fields.limit, 5);

        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        Map<String, Object> udafParam = new HashMap<>();
        udafParam.put(Fields.limit, 5);
        udafParam.put("aggregateType", "SUM");

        Map<String, Object> subUdafParam = new HashMap<>();
        subUdafParam.put("metricExpress", "amount");
        udafParam.put("udafParams", subUdafParam);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor = MetricUtil.getAggregateFieldProcessor(
                unitFactory, udafParam, fieldMap, "amount", "SUM");

        param.put(Fields.aggregateFieldProcessor, aggregateFieldProcessor);
        ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>> countWindowUnit = new ListObjectCountWindowUnit<>(param);
        countWindowUnit.add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 1L)));
        Object value = countWindowUnit.value();
        assertEquals(1L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 2L))));
        value = countWindowUnit.value();
        assertEquals(3L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 3L))));
        value = countWindowUnit.value();
        assertEquals(6L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 4L))));
        value = countWindowUnit.value();
        assertEquals(10L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 5L))));
        value = countWindowUnit.value();
        assertEquals(15L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 6L))));
        value = countWindowUnit.value();
        assertEquals(20L, value);

        countWindowUnit.merge(new ListObjectCountWindowUnit<Cloneable2Wrapper<JSONObject>>(param)
                .add(Cloneable2Wrapper.wrap(new JSONObject().set("amount", 7L))));
        value = countWindowUnit.value();
        assertEquals(25L, value);
    }

}