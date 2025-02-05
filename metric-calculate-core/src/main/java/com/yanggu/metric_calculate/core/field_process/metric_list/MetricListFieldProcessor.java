package com.yanggu.metric_calculate.core.field_process.metric_list;


import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.Map;

/**
 * 多表达式字段处理器
 */
@Getter
@EqualsAndHashCode
public class MetricListFieldProcessor implements FieldProcessor<Map<String, Object>, List<Object>> {

    private final Map<String, Class<?>> fieldMap;

    private final List<AviatorExpressParam> metricExpressParamList;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    public MetricListFieldProcessor(Map<String, Class<?>> fieldMap,
                                    List<AviatorExpressParam> metricExpressParamList,
                                    AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.metricExpressParamList = metricExpressParamList;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    @SneakyThrows
    public void init() {
        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        if (CollUtil.isEmpty(metricExpressParamList)) {
            throw new RuntimeException("表达式列表为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        this.metricFieldProcessorList = metricExpressParamList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress, aviatorFunctionFactory))
                .toList();
    }

    @SneakyThrows
    @Override
    public List<Object> process(Map<String, Object> input) {
        return this.metricFieldProcessorList.stream()
                .map(tempFieldProcessor -> tempFieldProcessor.process(input))
                .toList();
    }

}
