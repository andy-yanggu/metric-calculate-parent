package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getFieldOrderFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 多字段排序字段处理器
 */
class MultiFieldOrderFieldProcessorTest {

    @Test
    void init() {
    }

    @Test
    void process() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);

        List<FieldOrderParam> fieldOrderParamList = new ArrayList<>();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");
        fieldOrderParamList.add(new FieldOrderParam(aviatorExpressParam, true));
        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("age");
        fieldOrderParamList.add(new FieldOrderParam(aviatorExpressParam2, false));

        MultiFieldOrderFieldProcessor multiFieldOrderFieldProcessor = getFieldOrderFieldProcessor(fieldMap, fieldOrderParamList);

        JSONObject input = new JSONObject();
        input.set("name", "张三");
        input.set("age", 20);
        MultiFieldOrderCompareKey process = multiFieldOrderFieldProcessor.process(input);
        assertNotNull(process);
    }

}