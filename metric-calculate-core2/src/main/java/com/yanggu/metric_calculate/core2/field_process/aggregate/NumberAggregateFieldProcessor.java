package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NumberAggregateFieldProcessor<IN, ACC, OUT> extends AbstractAggregateFieldProcessor<IN, ACC, OUT> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private MetricFieldProcessor<Object> metricFieldProcessor;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    public NumberAggregateFieldProcessor(AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        super(aggregateFunction);
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getMetricExpress());
        //List<String> metricExpressList = udafParam.getMetricExpressList();
        //this.metricFieldProcessorList = metricExpressList.stream()
        //        .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress))
        //        .collect(Collectors.toList());
    }

    @Override
    public IN process(JSONObject input) {
        return (IN) metricFieldProcessor.process(input);
    }

}
