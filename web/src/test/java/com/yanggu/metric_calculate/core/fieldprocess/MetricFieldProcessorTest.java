package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 度量字段处理器单元测试类
 */
public class MetricFieldProcessorTest {

    /**
     * init方法应该对度量值进行编译
     *
     * @throws Exception
     */
    @Test
    public void init() throws Exception {
        MetricFieldProcessor<Object> objectMetricFieldProcessor = new MetricFieldProcessor<>();
        objectMetricFieldProcessor.setMetricExpress("amount");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        objectMetricFieldProcessor.setFieldMap(fieldMap);

        objectMetricFieldProcessor.init();

        assertEquals(AviatorEvaluator.compile("amount", true).toString(), objectMetricFieldProcessor.getMetricExpression().toString());

    }

    /**
     * 从明细数据中取出度量值
     *
     * @throws Exception
     */
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
        jsonObject.set("amount", "100");
        Object process = objectMetricFieldProcessor.process(jsonObject);

        assertEquals(BigDecimal.valueOf(100L), process);

    }

}