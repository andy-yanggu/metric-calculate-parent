package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParamItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 混合型字段处理器
 *
 * @param <IN>
 */
@Getter
@EqualsAndHashCode
public class MixFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final MixUdafParam mixUdafParam;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private final AggregateFunctionFactory aggregateFunctionFactory;

    private Map<String, FieldProcessor<JSONObject, Object>> multiBaseAggProcessorMap;

    public MixFieldProcessor(Map<String, Class<?>> fieldMap, MixUdafParam mixUdafParam, AviatorFunctionFactory aviatorFunctionFactory, AggregateFunctionFactory aggregateFunctionFactory) {
        this.fieldMap = fieldMap;
        this.mixUdafParam = mixUdafParam;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
        this.aggregateFunctionFactory = aggregateFunctionFactory;
    }

    @Override
    @SneakyThrows
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }
        if (mixUdafParam == null) {
            throw new RuntimeException("混合参数为空");
        }
        List<MixUdafParamItem> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();
        if (CollUtil.isEmpty(mixUdafParamItemList)) {
            throw new RuntimeException("基本聚合函数参数列表为空");
        }

        if (aggregateFunctionFactory == null) {
            throw new RuntimeException("聚合函数工厂类为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        Map<String, FieldProcessor<JSONObject, Object>> map = new HashMap<>();
        for (MixUdafParamItem mixUdafParamItem : mixUdafParamItemList) {
            FieldProcessor<JSONObject, Object> metricFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, mixUdafParamItem.getBaseUdafParam(), aviatorFunctionFactory, aggregateFunctionFactory);
            map.put(mixUdafParamItem.getName(), metricFieldProcessor);
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
