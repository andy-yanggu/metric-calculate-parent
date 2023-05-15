package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;


@Data
public class MixFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private MixUnitUdafParam mixUnitUdafParam;

    private Map<String, Class<?>> fieldMap;

    private AggregateFunctionFactory aggregateFunctionFactory;

    private Map<String, FieldProcessor<JSONObject, Object>> multiBaseAggProcessorMap;

    @Override
    @SneakyThrows
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }
        if (mixUnitUdafParam == null) {
            throw new RuntimeException("混合参数为空");
        }
        Map<String, BaseUdafParam> mixAggMap = mixUnitUdafParam.getMixAggMap();
        if (CollUtil.isEmpty(mixAggMap)) {
            throw new RuntimeException("map参数为空");
        }

        if (aggregateFunctionFactory == null) {
            throw new RuntimeException("聚合函数工厂类为空");
        }
        Map<String, FieldProcessor<JSONObject, Object>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            String paramName = entry.getKey();
            BaseUdafParam baseUdafParam = entry.getValue();
            AggregateFunction<Object, Object, Object> aggregateFunction = aggregateFunctionFactory.getAggregateFunction(baseUdafParam.getAggregateType());
            FieldProcessor<JSONObject, Object> metricFieldProcessor =
                    FieldProcessorUtil.getBaseFieldProcessor(baseUdafParam, fieldMap, aggregateFunction);
            map.put(paramName, metricFieldProcessor);
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
