package com.yanggu.metric_calculate.core2.field_process.multi_field_order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段排序字段处理器
 */
@Data
public class MultiFieldOrderFieldProcessor implements FieldProcessor<JSONObject, MultiFieldOrderCompareKey> {

    private Map<String, Class<?>> fieldMap;

    private List<FieldOrderParam> fieldOrderParamList;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(fieldOrderParamList)) {
            throw new RuntimeException("排序字段为空");
        }
        this.metricFieldProcessorList = fieldOrderParamList.stream()
                .map(tempFieldOrderParam -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempFieldOrderParam.getAviatorExpressParam(), aviatorFunctionFactory))
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldOrderCompareKey process(JSONObject input) throws Exception {
        int size = fieldOrderParamList.size();
        List<FieldOrder> fieldOrderList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FieldOrderParam fieldOrderParam = fieldOrderParamList.get(i);
            MetricFieldProcessor<Object> expression = metricFieldProcessorList.get(i);
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
