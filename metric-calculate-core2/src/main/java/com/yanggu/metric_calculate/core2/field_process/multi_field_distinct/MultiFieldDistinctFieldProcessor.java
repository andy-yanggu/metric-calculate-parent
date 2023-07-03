package com.yanggu.metric_calculate.core2.field_process.multi_field_distinct;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessorUtil;
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
public class MultiFieldDistinctFieldProcessor implements FieldProcessor<JSONObject, MultiFieldDistinctKey> {

    private List<AviatorExpressParam> distinctFieldListParamList;

    private Map<String, Class<?>> fieldMap;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(distinctFieldListParamList)) {
            throw new RuntimeException("去重字段表达式列表为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        this.metricFieldProcessorList = distinctFieldListParamList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress, aviatorFunctionFactory))
                .collect(Collectors.toList());
    }

    @Override
    public MultiFieldDistinctKey process(JSONObject input) throws Exception {
        List<Object> collect = metricFieldProcessorList.stream()
                .map(tempMetricExpress -> tempMetricExpress.process(input))
                .collect(Collectors.toList());
        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(collect);
        return multiFieldDistinctKey;
    }

}
