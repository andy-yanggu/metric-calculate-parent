package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import lombok.Data;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 混合型字段处理器
 *
 * @param <IN>
 */
@Data
public class MixFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private MixUdafParam mixUdafParam;

    private Map<String, Class<?>> fieldMap;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private AggregateFunctionFactory aggregateFunctionFactory;

    private Map<String, FieldProcessor<JSONObject, Object>> multiBaseAggProcessorMap;

    @Override
    @SneakyThrows
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }
        if (mixUdafParam == null) {
            throw new RuntimeException("混合参数为空");
        }
        Map<String, BaseUdafParam> mixAggMap = mixUdafParam.getMixAggMap();
        if (CollUtil.isEmpty(mixAggMap)) {
            throw new RuntimeException("map参数为空");
        }

        if (aggregateFunctionFactory == null) {
            throw new RuntimeException("聚合函数工厂类为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        Map<String, FieldProcessor<JSONObject, Object>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            FieldProcessor<JSONObject, Object> metricFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, entry.getValue(), aviatorFunctionFactory, aggregateFunctionFactory);
            map.put(entry.getKey(), metricFieldProcessor);
        }

        this.multiBaseAggProcessorMap = map;
    }

    @Override
    public IN process(JSONObject input) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        for (Map.Entry<String, FieldProcessor<JSONObject, Object>> entry : multiBaseAggProcessorMap.entrySet()) {
            String paramName = entry.getKey();
            FieldProcessor<JSONObject, Object> metricFieldProcessor = entry.getValue();
            Object process = metricFieldProcessor.process(input);
            dataMap.put(paramName, process);
        }

        return (IN) dataMap;
    }

}
