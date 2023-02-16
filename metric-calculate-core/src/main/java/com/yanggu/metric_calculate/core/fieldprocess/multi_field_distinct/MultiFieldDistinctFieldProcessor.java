package com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段去重字段处理器
 */
@Data
public class MultiFieldDistinctFieldProcessor implements FieldProcessor<JSONObject, MultiFieldDistinctKey> {

    private List<String> metricExpressList;

    private Map<String, Class<?>> fieldMap;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(metricExpressList)) {
            throw new RuntimeException("去重字段表达式列表为空");
        }
        this.metricFieldProcessorList = metricExpressList.stream()
                .map(tempExpress -> {
                    MetricFieldProcessor<Object> metricFieldProcessor = new MetricFieldProcessor<>();
                    metricFieldProcessor.setMetricExpress(tempExpress);
                    metricFieldProcessor.setFieldMap(fieldMap);
                    try {
                        metricFieldProcessor.init();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return metricFieldProcessor;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldDistinctKey process(JSONObject input) throws Exception {
        List<Object> collect = metricFieldProcessorList.stream()
                .map(tempMetricExpress -> {
                    try {
                        return tempMetricExpress.process(input);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(collect);
        return multiFieldDistinctKey;
    }

}
