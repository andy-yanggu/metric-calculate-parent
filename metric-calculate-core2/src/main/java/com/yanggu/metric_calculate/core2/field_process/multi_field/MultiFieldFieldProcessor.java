package com.yanggu.metric_calculate.core2.field_process.multi_field;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class MultiFieldFieldProcessor implements FieldProcessor<JSONObject, List<Object>> {

    private List<String> metricExpressList;

    private Map<String, Class<?>> fieldMap;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(metricExpressList)) {
            throw new RuntimeException("度量字段列表为空");
        }
        this.metricFieldProcessorList = metricExpressList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress))
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> process(JSONObject input) throws Exception {
        List<Object> dataList = new ArrayList<>();
        for (MetricFieldProcessor<Object> fieldProcessor : this.metricFieldProcessorList) {
            Object tempData = fieldProcessor.process(input);
            if (tempData == null) {
                throw new RuntimeException("度量字段的值不能为空, 度量表达式: " + fieldProcessor.getMetricExpress());
            }
            dataList.add(tempData);
        }
        return dataList;
    }

}
