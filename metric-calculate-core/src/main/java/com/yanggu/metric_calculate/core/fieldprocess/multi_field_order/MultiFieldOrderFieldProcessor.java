package com.yanggu.metric_calculate.core.fieldprocess.multi_field_order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段排序字段处理器
 */
@Data
public class MultiFieldOrderFieldProcessor<T> implements FieldProcessor<T, MultiFieldOrderCompareKey> {

    private Map<String, Class<?>> fieldMap;

    private List<FieldOrderParam> fieldOrderParamList;

    private List<MetricFieldProcessor<T, Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(fieldOrderParamList)) {
            throw new RuntimeException("排序字段为空");
        }
        this.metricFieldProcessorList = fieldOrderParamList.stream()
                .map(tempFieldOrderParam ->
                        FieldProcessorUtil.<T>getMetricFieldProcessor(fieldMap, tempFieldOrderParam.getExpress()))
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldOrderCompareKey process(T input) throws Exception {

        int size = fieldOrderParamList.size();
        List<FieldOrder> fieldOrderList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FieldOrderParam fieldOrderParam = fieldOrderParamList.get(i);
            MetricFieldProcessor<T, Object> expression = metricFieldProcessorList.get(i);
            Object execute = expression.process(input);
            FieldOrder fieldOrder = new FieldOrder();
            fieldOrder.setResult(execute);
            fieldOrder.setAsc(fieldOrderParam.getAsc());
            fieldOrderList.add(fieldOrder);
        }
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setFieldOrderList(fieldOrderList);
        return multiFieldOrderCompareKey;
    }

}
