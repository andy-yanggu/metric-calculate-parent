package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParamItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;

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
public class MixFieldProcessor<IN> implements FieldProcessor<Map<String, Object>, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final MixUdafParam mixUdafParam;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private final AggregateFunctionFactory aggregateFunctionFactory;

    private Map<String, FieldProcessor<Map<String, Object>, Object>> multiBaseAggProcessorMap;

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

        Map<String, FieldProcessor<Map<String, Object>, Object>> map = new HashMap<>();
        for (MixUdafParamItem mixUdafParamItem : mixUdafParamItemList) {
            FieldProcessor<Map<String, Object>, Object> metricFieldProcessor = null;
            BaseUdafParam baseUdafParam = mixUdafParamItem.getBaseUdafParam();
            if (baseUdafParam != null) {
                metricFieldProcessor = FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, baseUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            }
            MapUdafParam mapUdafParam = mixUdafParamItem.getMapUdafParam();
            if (mapUdafParam != null) {
                metricFieldProcessor = FieldProcessorUtil.getMapFieldProcessor(fieldMap, mapUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            }
            if (metricFieldProcessor == null) {
                throw new RuntimeException("MixUdafParamItem中聚合函数参数错误");
            }
            map.put(mixUdafParamItem.getName(), metricFieldProcessor);
        }

        this.multiBaseAggProcessorMap = map;
    }

    @Override
    public IN process(Map<String, Object> input) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        for (Map.Entry<String, FieldProcessor<Map<String, Object>, Object>> entry : multiBaseAggProcessorMap.entrySet()) {
            String paramName = entry.getKey();
            FieldProcessor<Map<String, Object>, Object> metricFieldProcessor = entry.getValue();
            Object process = metricFieldProcessor.process(input);
            dataMap.put(paramName, process);
        }

        return (IN) dataMap;
    }

}
