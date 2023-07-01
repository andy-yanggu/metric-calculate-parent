package com.yanggu.metric_calculate.core2.field_process.metric_list;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MetricListFieldProcessorTest {

    @Test
    public void init1() {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricListFieldProcessor::init);
        assertEquals("表达式列表为空", runtimeException.getMessage());
    }

    @Test
    public void init2() {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("test1");
        metricListFieldProcessor.setMetricExpressParamList(CollUtil.toList(aviatorExpressParam));
        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricListFieldProcessor::init);
        assertEquals("宽表字段为空", runtimeException.getMessage());
    }

    @Test
    public void process() throws Exception {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("test1", String.class);
        fieldMap.put("test2", String.class);

        metricListFieldProcessor.setFieldMap(fieldMap);
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("test1");
        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("test2");
        metricListFieldProcessor.setMetricExpressParamList(CollUtil.toList(aviatorExpressParam1, aviatorExpressParam2));
        metricListFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("test1", "aaa");
        input.set("test2", "bbb");

        List<Object> process = metricListFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals("aaa", process.get(0));
        assertEquals("bbb", process.get(1));
    }

}