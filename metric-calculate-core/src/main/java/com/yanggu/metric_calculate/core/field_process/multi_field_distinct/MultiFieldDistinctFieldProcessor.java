package com.yanggu.metric_calculate.core.field_process.multi_field_distinct;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多字段去重字段处理器
 */
@Getter
@EqualsAndHashCode
public class MultiFieldDistinctFieldProcessor implements FieldProcessor<JSONObject, MultiFieldDistinctKey> {

    private final Map<String, Class<?>> fieldMap;

    private final List<AviatorExpressParam> distinctFieldListParamList;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    public MultiFieldDistinctFieldProcessor(Map<String, Class<?>> fieldMap,
                                            List<AviatorExpressParam> distinctFieldListParamList,
                                            AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.distinctFieldListParamList = distinctFieldListParamList;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

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
