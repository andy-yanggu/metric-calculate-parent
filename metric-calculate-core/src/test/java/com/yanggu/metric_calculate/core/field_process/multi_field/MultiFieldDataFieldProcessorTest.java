package com.yanggu.metric_calculate.core.field_process.multi_field;

import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.field_process.FieldProcessorTestBase.getMultiFieldDataFieldFieldProcessor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * 多字段去重字段处理器单元测试类
 */
@DisplayName("多字段去重字段处理器单元测试类")
class MultiFieldDataFieldProcessorTest {

    @Test
    void init() {
    }

    @ParameterizedTest
    @DisplayName("验证process方法")
    @CsvSource({"'张三',20", "'李四',30"})
    void process(String name, Integer age) throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("name", String.class);
        fieldMap.put("age", Integer.class);

        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("name");

        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("age");
        MultiFieldDataFieldProcessor multiFieldDataFieldProcessor = getMultiFieldDataFieldFieldProcessor(fieldMap, List.of(aviatorExpressParam, aviatorExpressParam2));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        paramMap.put("age", age);
        MultiFieldData process = multiFieldDataFieldProcessor.process(paramMap);

        assertNotNull(process);
        assertEquals(List.of(name, age), process.getFieldList());
    }

}