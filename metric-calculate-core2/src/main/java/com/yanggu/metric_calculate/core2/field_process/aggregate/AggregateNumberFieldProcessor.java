package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.unit.AggregateFunction;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class AggregateNumberFieldProcessor<IN, ACC, OUT> implements AggregateFieldProcessor<IN, ACC, OUT> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private MetricFieldProcessor<Object> metricFieldProcessor;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        this.metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getMetricExpress());
        List<String> metricExpressList = udafParam.getMetricExpressList();
        this.metricFieldProcessorList = metricExpressList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress))
                .collect(Collectors.toList());
    }

    @Override
    public IN process(JSONObject input) {
        return (IN) metricFieldProcessor.process(input);
    }

    @Override
    public AggregateFunction<IN, ACC, OUT> getAggregateFunction() {
        return null;
    }

}
