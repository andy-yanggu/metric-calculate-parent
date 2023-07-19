package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.field_process.FieldProcessorTestBase.getBaseAggregateFieldProcessor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * NumberFieldProcessor单元测试类
 * <p>数值型聚合字段处理器单元测试类</p>
 */
class NumberFieldProcessorTest {

    @Test
    void testProcess1() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);

        String aggregateType = "SUM";
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        AviatorExpressParam aviatorExpressParam = new AviatorExpressParam();
        aviatorExpressParam.setExpress("amount");
        baseUdafParam.setMetricExpressParam(aviatorExpressParam);
        baseUdafParam.setAggregateType(aggregateType);

        FieldProcessor<JSONObject, Double> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("amount", 100.0D);
        Double process = baseFieldProcessor.process(jsonObject);
        assertEquals(100.0D, process, 0.0D);
    }

    /**
     * 测试协方差CovUnit
     *
     * @throws Exception
     */
    @Test
    void testProcess_CovUnit() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("COV");
        AviatorExpressParam aviatorExpressParam1 = new AviatorExpressParam();
        aviatorExpressParam1.setExpress("amount");

        AviatorExpressParam aviatorExpressParam2 = new AviatorExpressParam();
        aviatorExpressParam2.setExpress("amount1");
        baseUdafParam.setMetricExpressParamList(Arrays.asList(aviatorExpressParam1, aviatorExpressParam2));

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);
        fieldMap.put("amount1", Long.class);

        FieldProcessor<JSONObject, List<? extends Number>> baseFieldProcessor = getBaseAggregateFieldProcessor(fieldMap, baseUdafParam);

        JSONObject input = new JSONObject();
        input.set("amount", 1L);
        input.set("amount1", 2L);

        List<? extends Number> process = baseFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals(1L, process.get(0));
        assertEquals(2L, process.get(1));
    }

}