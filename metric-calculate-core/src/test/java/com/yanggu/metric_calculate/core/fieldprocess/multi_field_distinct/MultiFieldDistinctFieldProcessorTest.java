package com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct;

import cn.hutool.json.JSONObject;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 多字段去重字段处理器
 */
public class MultiFieldDistinctFieldProcessorTest {

    @Test
    public void init() {
    }

    @Test
    public void process() throws Exception {
        MultiFieldDistinctFieldProcessor multiFieldDistinctFieldProcessor = new MultiFieldDistinctFieldProcessor();

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        multiFieldDistinctFieldProcessor.setFieldMap(fieldMap);

        List<String> metricList = Collections.singletonList("name");
        multiFieldDistinctFieldProcessor.setMetricExpressList(metricList);

        multiFieldDistinctFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldDistinctKey process = multiFieldDistinctFieldProcessor.process(input);
        assertNotNull(process);
        assertEquals(Collections.singletonList("张三"), process.getFieldList());
    }

}