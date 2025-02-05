package com.yanggu.metric_calculate.core.field_process.metric_list;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMetricListFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> getMetricListFieldProcessor(fieldMap, ListUtil.of(aviatorExpressParam), null));
        assertEquals("Aviator函数工厂类为空", runtimeException.getMessage());
    }

    @Test
    void process() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        fieldMap.put("test2", String.class);
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("test1");
        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("test2");
        MetricListFieldProcessor metricListFieldProcessor = getMetricListFieldProcessor(fieldMap, ListUtil.of(aviatorExpressParam1, aviatorExpressParam2));

        Map<String, Object> input = new HashMap<>();
        input.put("test1", "aaa");
        input.put("test2", "bbb");

        List<Object> process = metricListFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals("aaa", process.get(0));
        assertEquals("bbb", process.get(1));
    }

}