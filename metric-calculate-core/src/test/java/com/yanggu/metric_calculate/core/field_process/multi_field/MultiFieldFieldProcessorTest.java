package com.yanggu.metric_calculate.core.field_process.multi_field;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getDistinctFieldFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * 多字段去重字段处理器
 */
class MultiFieldFieldProcessorTest {

    @Test
    void init() {
    }

    @Test
    void process() throws Exception {

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        List<AviatorExpressParam> metricList = Collections.singletonList(aviatorExpressParam);
        MultiFieldFieldProcessor multiFieldFieldProcessor = getDistinctFieldFieldProcessor(fieldMap, metricList);

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldData process = multiFieldFieldProcessor.process(input);
        assertNotNull(process);
        assertEquals(Collections.singletonList("张三"), process.getFieldList());
    }

}