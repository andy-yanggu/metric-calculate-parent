package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactoryTest.getAggregateFunctionFactory;
import static org.junit.Assert.assertEquals;

/**
 * NumberFieldProcessor单元测试类
 * <p>数值型聚合字段处理器单元测试类</p>
 */
public class NumberFieldProcessorTest {

    @Test
    public void testProcess1() throws Exception {
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Double.class);

        String aggregateType = "SUM";
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setMetricExpress("amount");
        baseUdafParam.setAggregateType(aggregateType);

        FieldProcessor<JSONObject, Double> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

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
    public void testProcess_CovUnit() throws Exception {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("COV");
        baseUdafParam.setMetricExpressList(Arrays.asList("amount", "amount1"));

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amount", Long.class);
        fieldMap.put("amount1", Long.class);

        FieldProcessor<JSONObject, List<? extends Number>> baseFieldProcessor = FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, getAggregateFunctionFactory());

        JSONObject input = new JSONObject();
        input.set("amount", 1L);
        input.set("amount1", 2L);

        List<? extends Number> process = baseFieldProcessor.process(input);
        assertEquals(2, process.size());
        assertEquals(1L, process.get(0));
        assertEquals(2L, process.get(1));

    }

}