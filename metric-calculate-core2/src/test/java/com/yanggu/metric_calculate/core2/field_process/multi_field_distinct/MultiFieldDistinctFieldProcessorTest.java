package com.yanggu.metric_calculate.core2.field_process.multi_field_distinct;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.field_process.FieldProcessorTestBase.getDistinctFieldFieldProcessor;
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

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        List<AviatorExpressParam> metricList = Collections.singletonList(aviatorExpressParam);
        MultiFieldDistinctFieldProcessor multiFieldDistinctFieldProcessor = getDistinctFieldFieldProcessor(fieldMap, metricList);

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldDistinctKey process = multiFieldDistinctFieldProcessor.process(input);
        assertNotNull(process);
        assertEquals(Collections.singletonList("张三"), process.getFieldList());
    }

}