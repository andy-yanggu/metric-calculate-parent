package com.yanggu.metric_calculate.core.field_process.metric_list;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.json.JSONObject;
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
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricListFieldProcessor::init);
        assertEquals("表达式列表为空", runtimeException.getMessage());
    }

    @Test
    void init2() {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test1");
        metricListFieldProcessor.setMetricExpressParamList(ListUtil.of(aviatorExpressParam));
        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricListFieldProcessor::init);
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    void init3() {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        metricListFieldProcessor.setFieldMap(fieldMap);
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test1");
        metricListFieldProcessor.setMetricExpressParamList(ListUtil.of(aviatorExpressParam));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricListFieldProcessor::init);
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

        JSONObject input = new JSONObject();
        input.set("test1", "aaa");
        input.set("test2", "bbb");

        List<Object> process = metricListFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals("aaa", process.get(0));
        assertEquals("bbb", process.get(1));
    }

}