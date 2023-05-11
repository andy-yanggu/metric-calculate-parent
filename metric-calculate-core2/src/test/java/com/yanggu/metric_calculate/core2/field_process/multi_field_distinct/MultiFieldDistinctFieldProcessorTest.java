package com.yanggu.metric_calculate.core2.field_process.multi_field_distinct;

import cn.hutool.json.JSONObject;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        multiFieldDistinctFieldProcessor.setDistinctFieldList(metricList);

        multiFieldDistinctFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldDistinctKey process = multiFieldDistinctFieldProcessor.process(input);
        assertNotNull(process);
        assertEquals(Collections.singletonList("张三"), process.getFieldList());
    }

}