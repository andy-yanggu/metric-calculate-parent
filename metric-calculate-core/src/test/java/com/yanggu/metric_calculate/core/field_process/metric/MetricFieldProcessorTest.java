package com.yanggu.metric_calculate.core.field_process.metric;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * 度量字段处理器单元测试类
 */
public class MetricFieldProcessorTest {

    /**
     * 没有传递metricExpress, 应该报错
     */
    @Test
    public void init1() {
        MetricFieldProcessor<Object> metricFieldProcessor = new MetricFieldProcessor<>();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricFieldProcessor::init);
        assertEquals("度量表达式为空", runtimeException.getMessage());
    }

    /**
     * 没有传递fieldMap, 应该报错
     */
    @Test
    public void init2() {
        MetricFieldProcessor<Object> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setMetricExpress("metricExpress");

        RuntimeException runtimeException = assertThrows(RuntimeException.class, metricFieldProcessor::init);
        assertEquals("明细宽表字段map为空", runtimeException.getMessage());
    }

    /**
     * 如果度量表达式的变量没有在宽表字段中, 应该报错
     *
     * @throws Exception
     */
    @Test
    public void init3() throws Exception {
        String metricExpress = "amount";
        MetricFieldProcessor<Object> objectMetricFieldProcessor = new MetricFieldProcessor<>();
        objectMetricFieldProcessor.setMetricExpress(metricExpress);

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount2", BigDecimal.class);
        objectMetricFieldProcessor.setFieldMap(fieldMap);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, objectMetricFieldProcessor::init);
        assertEquals("数据明细宽表中没有该度量字段: " + metricExpress, runtimeException.getMessage());

    }

    /**
     * init方法应该对度量值进行编译
     *
     * @throws Exception
     */
    @Test
    public void init4() throws Exception {
        MetricFieldProcessor<Object> objectMetricFieldProcessor = new MetricFieldProcessor<>();
        objectMetricFieldProcessor.setMetricExpress("amount");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", BigDecimal.class);
        objectMetricFieldProcessor.setFieldMap(fieldMap);

        objectMetricFieldProcessor.init();

        assertEquals(AviatorEvaluator.compile("amount", true).toString(), objectMetricFieldProcessor.getMetricExpression().toString());
        assertEquals("amount", objectMetricFieldProcessor.getMetricExpress());
        assertEquals(fieldMap, objectMetricFieldProcessor.getFieldMap());

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