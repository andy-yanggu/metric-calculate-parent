package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactoryTest.getAggregateFunctionFactory;
import static org.junit.Assert.*;

public class MixFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @Before
    public void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("city", String.class);
        this.fieldMap = fieldMap;
    }

    @Test
    public void testInit1() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    public void testInit2() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("混合参数为空", runtimeException.getMessage());
    }

    @Test
    public void testInit3() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        mixFieldProcessor.setMixUdafParam(new MixUdafParam());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("map参数为空", runtimeException.getMessage());
    }

    @Test
    public void testInit4() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        mixFieldProcessor.setMixUdafParam(mixUdafParam);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("聚合函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    public void testInit5() throws Exception {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        mixFieldProcessor.setMixUdafParam(mixUdafParam);
        mixFieldProcessor.setAggregateFunctionFactory(getAggregateFunctionFactory());

        mixFieldProcessor.init();

        assertSame(fieldMap, mixFieldProcessor.getFieldMap());
        assertSame(mixUdafParam, mixFieldProcessor.getMixUdafParam());
        assertSame(getAggregateFunctionFactory(), mixFieldProcessor.getAggregateFunctionFactory());
        assertTrue(CollUtil.isNotEmpty(mixFieldProcessor.getMultiBaseAggProcessorMap()));
    }

    @Test
    public void process() throws Exception {

        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);

        MixFieldProcessor<Map<String, Long>> mixFieldProcessor = FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUdafParam, getAggregateFunctionFactory());

        JSONObject input1 = new JSONObject();
        input1.set("amount", 100L);
        input1.set("city", "上海");

        Map<String, Long> process = mixFieldProcessor.process(input1);

        assertEquals(2, process.size());
        assertEquals(100L, process.get("上海_sum").longValue());
        assertEquals(100L, process.get("全国_sum").longValue());

        JSONObject input2 = new JSONObject();
        input2.set("amount", 200L);
        input2.set("city", "北京");
        process = mixFieldProcessor.process(input2);
        assertEquals(2, process.size());
        assertEquals(0L, process.get("上海_sum").longValue());
        assertEquals(200L, process.get("全国_sum").longValue());
    }

}