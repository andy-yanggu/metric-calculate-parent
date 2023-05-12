package com.yanggu.metric_calculate.core2.field_process.metric_list;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多表达式字段处理器
 */
@Data
public class MetricListFieldProcessor implements FieldProcessor<JSONObject, List<Object>> {

    private List<String> metricExpressList;

    private Map<String, Class<?>> fieldMap;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @SneakyThrows
    @Override
    public void init() {
        if (CollUtil.isEmpty(metricExpressList)) {
            throw new RuntimeException("表达式列表为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }
        this.metricFieldProcessorList = metricExpressList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List<Object> process(JSONObject input) {
        return this.metricFieldProcessorList.stream()
                .map(tempFieldProcessor -> tempFieldProcessor.process(input))
                .collect(Collectors.toList());
    }

}
