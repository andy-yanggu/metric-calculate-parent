package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionFactoryTest.getAggregateFunctionFactory;
import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMixFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

class MixFieldProcessorTest {

    private Map<String, Class<?>> fieldMap;

    @BeforeEach
    void init() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);
        fieldMap.put("city", String.class);
        this.fieldMap = fieldMap;
    }

    @Test
    void testInit1() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    void testInit2() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("混合参数为空", runtimeException.getMessage());
    }

    @Test
    void testInit3() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        mixFieldProcessor.setMixUdafParam(new MixUdafParam());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("map参数为空", runtimeException.getMessage());
    }

    @Test
    void testInit4() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        mixFieldProcessor.setMixUdafParam(mixUdafParam);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("聚合函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    void testInit5() {
        MixFieldProcessor<Object> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        mixFieldProcessor.setAggregateFunctionFactory(getAggregateFunctionFactory());
        mixFieldProcessor.setMixUdafParam(mixUdafParam);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, mixFieldProcessor::init);
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    void testInit6() throws Exception {
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        MixFieldProcessor<Object> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam);

        assertSame(fieldMap, mixFieldProcessor.getFieldMap());
        assertSame(mixUdafParam, mixFieldProcessor.getMixUdafParam());
        assertSame(getAggregateFunctionFactory(), mixFieldProcessor.getAggregateFunctionFactory());
        assertTrue(CollUtil.isNotEmpty(mixFieldProcessor.getMultiBaseAggProcessorMap()));
    }

    @Test
    void process() throws Exception {

        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);

        MixFieldProcessor<Map<String, Long>> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam);

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