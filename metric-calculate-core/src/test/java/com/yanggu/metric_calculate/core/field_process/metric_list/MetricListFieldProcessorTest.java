package com.yanggu.metric_calculate.core.field_process.metric_list;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMetricListFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("多表达式字段处理器单元测试类")
class MetricListFieldProcessorTest {

    @Test
    void init1() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricListFieldProcessor(null, null));
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    void init2() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricListFieldProcessor(fieldMap, null));
        assertEquals("表达式列表为空", runtimeException.getMessage());
    }

    @Test
    void init3() {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test1");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricListFieldProcessor(fieldMap, List.of(aviatorExpressParam), null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }

    @ParameterizedTest
    @DisplayName("验证process方法")
    @CsvSource({"'aaa','bbb'", "'ccc','ddd'"})
    void process(String param1, String param2) {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        fieldMap.put("test2", String.class);

        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("test1");
        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("test2");
        MetricListFieldProcessor metricListFieldProcessor = getMetricListFieldProcessor(fieldMap, List.of(aviatorExpressParam1, aviatorExpressParam2));

        Map<String, Object> input = new HashMap<>();
        input.put("test1", param1);
        input.put("test2", param2);

        List<Object> process = metricListFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals(param1, process.get(0));
        assertEquals(param2, process.get(1));
    }

}