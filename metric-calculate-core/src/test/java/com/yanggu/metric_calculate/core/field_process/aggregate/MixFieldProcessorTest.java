package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.AVIATOR_FUNCTION_FACTORY;
import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMixFieldProcessor;
import static com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactoryTest.getAggregateFunctionFactory;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 混合型字段处理器单元测试类
 */
@DisplayName("混合型字段处理器单元测试类")
class MixFieldProcessorTest {

    private static Map<String, Class<?>> fieldMap;

    private static MixFieldProcessor<Map<String, Long>> mixFieldProcessor;

    @BeforeAll
    static void init() {
        fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);
        fieldMap.put("city", String.class);

        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam);
    }

    @Test
    void testInit1() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMixFieldProcessor(null, null));
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    void testInit2() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMixFieldProcessor(fieldMap, null));
        assertEquals("混合参数为空", runtimeException.getMessage());
    }

    @Test
    void testInit3() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMixFieldProcessor(fieldMap, new MixUdafParam()));
        assertEquals("基本聚合函数参数列表为空", runtimeException.getMessage());
    }

    @Test
    void testInit4() {
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMixFieldProcessor(fieldMap, mixUdafParam, null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    void testInit5() {
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMixFieldProcessor(fieldMap, mixUdafParam, AVIATOR_FUNCTION_FACTORY, null));
        assertEquals("聚合函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    void testInit6() {
        String jsonString = FileUtil.readUtf8String("test_mix_unit_udaf_param.json");
        MixUdafParam mixUdafParam = JSONUtil.toBean(jsonString, MixUdafParam.class);
        MixFieldProcessor<Object> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam);

        assertSame(fieldMap, mixFieldProcessor.getFieldMap());
        assertSame(mixUdafParam, mixFieldProcessor.getMixUdafParam());
        assertSame(getAggregateFunctionFactory(), mixFieldProcessor.getAggregateFunctionFactory());
        assertTrue(CollUtil.isNotEmpty(mixFieldProcessor.getMultiBaseAggProcessorMap()));
    }

    /**
     * 混合型字段处理器测试
     */
    @ParameterizedTest
    @DisplayName("测试混合聚合函数")
    @CsvSource({"100,'上海',100,100", "200,'北京',0,200"})
    void process(Long amount, String city, Long shanghaiAmount, Long nationalAmount) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("amount", amount);
        paramMap.put("city", city);

        Map<String, Long> process = mixFieldProcessor.process(paramMap);
        assertNotNull(process);
        assertInstanceOf(LinkedHashMap.class, process);
        assertEquals(2, process.size());
        assertEquals(shanghaiAmount, process.get("上海_sum"));
        assertEquals(nationalAmount, process.get("全国_sum"));
    }

}