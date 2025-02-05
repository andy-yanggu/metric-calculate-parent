package com.yanggu.metric_calculate.core.field_process.multi_field;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getDistinctFieldFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * 多字段去重字段处理器
 */
class MultiFieldDataFieldProcessorTest {

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
        MultiFieldDataFieldProcessor multiFieldDataFieldProcessor = getDistinctFieldFieldProcessor(fieldMap, List.of(aviatorExpressParam));

        Map<String, Object> input = new HashMap<>();
        input.put("name", "张三");
        input.put("age", 20);
        MultiFieldData process = multiFieldDataFieldProcessor.process(input);
        assertNotNull(process);
        assertEquals(List.of("张三"), process.getFieldList());
    }

}