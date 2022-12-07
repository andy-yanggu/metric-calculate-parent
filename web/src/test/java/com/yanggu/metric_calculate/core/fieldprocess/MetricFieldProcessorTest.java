package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.*;

public class MetricFieldProcessorTest {

    @Test
    public void init() throws Exception {
        MetricFieldProcessor<Object> objectMetricFieldProcessor = new MetricFieldProcessor<>();
        objectMetricFieldProcessor.setMetricExpress("amount");
        objectMetricFieldProcessor.init();

        assertEquals(AviatorEvaluator.compile("amount", true).toString(), objectMetricFieldProcessor.getMetricExpression().toString());

    }

    @Test
    public void process() throws Exception {
        MetricFieldProcessor<Object> objectMetricFieldProcessor = new MetricFieldProcessor<>();
        objectMetricFieldProcessor.setMetricExpress("amount");
        HashMap<String, Class<?>> fieldMap = new HashMap<String, Class<?>>() {{
            put("amount", BigDecimal.class);
        }};
        objectMetricFieldProcessor.setFieldMap(fieldMap);
        objectMetricFieldProcessor.init();

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 100);
        Object process = objectMetricFieldProcessor.process(jsonObject);

        assertEquals(BigDecimal.valueOf(100L), process);

    }

}