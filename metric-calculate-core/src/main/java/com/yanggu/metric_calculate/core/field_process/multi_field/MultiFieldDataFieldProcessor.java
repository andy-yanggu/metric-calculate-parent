package com.yanggu.metric_calculate.core.field_process.multi_field;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.Map;

/**
 * 多字段字段处理器
 */
@Getter
@EqualsAndHashCode
public class MultiFieldDataFieldProcessor implements FieldProcessor<Map<String, Object>, MultiFieldData> {

    private final Map<String, Class<?>> fieldMap;

    private final List<AviatorExpressParam> aviatorExpressParamList;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    public MultiFieldDataFieldProcessor(Map<String, Class<?>> fieldMap,
                                        List<AviatorExpressParam> aviatorExpressParamList,
                                        AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.aviatorExpressParamList = aviatorExpressParamList;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    public void init() throws Exception {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("明细宽表字段map为空");
        }

        if (CollUtil.isEmpty(aviatorExpressParamList)) {
            throw new RuntimeException("去重字段表达式列表为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        this.metricFieldProcessorList = aviatorExpressParamList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress, aviatorFunctionFactory))
                .toList();
    }

    @Override
    public MultiFieldData process(Map<String, Object> input) throws Exception {
        List<Object> collect = metricFieldProcessorList.stream()
                .map(tempMetricExpress -> tempMetricExpress.process(input))
                .toList();
        MultiFieldData multiFieldData = new MultiFieldData();
        multiFieldData.setFieldList(collect);
        return multiFieldData;
    }

}
