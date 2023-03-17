package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.annotation.Numerical;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class NumberFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private Numerical numerical;

    private MetricFieldProcessor<Number> metricFieldProcessor;

    private List<MetricFieldProcessor<Number>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (numerical.multiNumber()) {
            List<String> metricExpressList = udafParam.getMetricExpressList();
            if (CollUtil.isEmpty(metricExpressList)) {
                throw new RuntimeException("度量字段列表为空");
            }
            this.metricFieldProcessorList = metricExpressList.stream()
                    .map(tempExpress -> FieldProcessorUtil.<Number>getMetricFieldProcessor(fieldMap, tempExpress))
                    .collect(Collectors.toList());
        } else {
            String metricExpress = udafParam.getMetricExpress();
            if (StrUtil.isBlank(metricExpress)) {
                throw new RuntimeException("度量字段为空");
            }
            this.metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, metricExpress);
        }
    }

    @Override
    public IN process(JSONObject input) {
        Object process;
        if (numerical.multiNumber()) {
            List<Number> dataList = new ArrayList<>();
            for (MetricFieldProcessor<Number> fieldProcessor : this.metricFieldProcessorList) {
                Number tempData = fieldProcessor.process(input);
                if (tempData == null) {
                    throw new RuntimeException("度量值为空, 度量表达式" + fieldProcessor.getMetricExpress());
                }
                dataList.add(tempData);
            }
            process = dataList;
        } else {
            process = this.metricFieldProcessor.process(input);
        }
        return (IN) process;
    }

}
