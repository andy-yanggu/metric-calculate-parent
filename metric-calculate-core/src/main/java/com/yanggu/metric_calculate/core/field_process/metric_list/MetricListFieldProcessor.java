package com.yanggu.metric_calculate.core.field_process.metric_list;


import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多表达式字段处理器
 */
@Data
public class MetricListFieldProcessor implements FieldProcessor<JSONObject, List<Object>> {

    private List<AviatorExpressParam> metricExpressParamList;

    private Map<String, Class<?>> fieldMap;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private List<MetricFieldProcessor<Object>> metricFieldProcessorList;

    @SneakyThrows
    @Override
    public void init() {
        if (CollUtil.isEmpty(metricExpressParamList)) {
            throw new RuntimeException("表达式列表为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("Aviator函数工厂类为空");
        }

        this.metricFieldProcessorList = metricExpressParamList.stream()
                .map(tempExpress -> FieldProcessorUtil.getMetricFieldProcessor(fieldMap, tempExpress, aviatorFunctionFactory))
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
