package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class MixFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private MixUnitUdafParam mixUnitUdafParam;

    private Map<String, Class<?>> fieldMap;

    private Map<String, MetricFieldProcessor<Object>> multiBaseAggProcessorMap;

    @Override
    public void init() throws Exception {
        Map<String, BaseUdafParam> mixAggMap = mixUnitUdafParam.getMixAggMap();

        Map<String, MetricFieldProcessor<Object>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            String paramName = entry.getKey();
            BaseUdafParam baseUdafParam = entry.getValue();
            MetricFieldProcessor<Object> metricFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, baseUdafParam.getMetricExpress());
            map.put(paramName, metricFieldProcessor);
        }

        this.multiBaseAggProcessorMap = map;
    }

    @Override
    public IN process(JSONObject input) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        multiBaseAggProcessorMap.forEach((paramName, metricFieldProcessor) -> {
            Object process = metricFieldProcessor.process(input);
            dataMap.put(paramName, process);
        });

        return (IN) dataMap;
    }

}
