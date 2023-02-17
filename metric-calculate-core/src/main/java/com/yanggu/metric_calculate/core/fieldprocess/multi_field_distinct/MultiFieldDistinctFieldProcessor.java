package com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段去重字段处理器
 */
@Data
@NoArgsConstructor
public class MultiFieldDistinctFieldProcessor<T> implements FieldProcessor<T, MultiFieldDistinctKey> {

    private List<String> metricExpressList;

    private Map<String, Class<?>> fieldMap;

    private List<MetricFieldProcessor<T, Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(metricExpressList)) {
            throw new RuntimeException("去重字段表达式列表为空");
        }
        this.metricFieldProcessorList = metricExpressList.stream()
                .map(tempExpress -> FieldProcessorUtil.<T>getMetricFieldProcessor(fieldMap, tempExpress))
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldDistinctKey process(T input) throws Exception {
        List<Object> collect = metricFieldProcessorList.stream()
                .map(tempMetricExpress -> tempMetricExpress.process(input))
                .collect(Collectors.toList());
        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(collect);
        return multiFieldDistinctKey;
    }

}
