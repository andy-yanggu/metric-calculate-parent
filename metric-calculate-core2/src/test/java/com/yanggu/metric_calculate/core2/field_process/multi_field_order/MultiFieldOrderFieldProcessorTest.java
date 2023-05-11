package com.yanggu.metric_calculate.core2.field_process.multi_field_order;

import cn.hutool.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * 多字段排序字段处理器
 */
public class MultiFieldOrderFieldProcessorTest {

    @Test
    public void init() {
    }

    @Test
    public void process() throws Exception {
        MultiFieldOrderFieldProcessor multiFieldOrderFieldProcessor = new MultiFieldOrderFieldProcessor();
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);
        multiFieldOrderFieldProcessor.setFieldMap(fieldMap);

        List<FieldOrderParam> fieldOrderParamList = new ArrayList<>();
        fieldOrderParamList.add(new FieldOrderParam("name", true));
        fieldOrderParamList.add(new FieldOrderParam("age", false));
        multiFieldOrderFieldProcessor.setFieldOrderParamList(fieldOrderParamList);

        multiFieldOrderFieldProcessor.init();

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldOrderCompareKey process = multiFieldOrderFieldProcessor.process(input);
        assertNotNull(process);
    }

}