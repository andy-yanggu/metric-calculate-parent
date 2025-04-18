package com.yanggu.metric_calculate.core.calculate.field;

import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.util.AviatorExpressUtil;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 虚拟字段计算类
 * <p>有些字段是虚拟字段通过其他字段计算得到</p>
 * <p>常见的根据时间戳，得到年、月、日、小时数等</p>
 * <p>可以实现简单的字段补全逻辑</p>
 */
@Data
public class VirtualFieldCalculate<R> implements FieldCalculate<Map<String, Object>, R> {

    private String columnName;

    private AviatorExpressParam aviatorExpressParam;

    private Map<String, Class<?>> fieldMap;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private MetricFieldProcessor<R> metricFieldProcessor;

    @Override
    public void init() {
        this.metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, aviatorExpressParam, aviatorFunctionFactory);
    }

    @Override
    public Set<String> dependFields() {
        List<String> variableNames = AviatorExpressUtil.compileExpress(aviatorExpressParam, aviatorFunctionFactory).getVariableNames();
        return new HashSet<>(variableNames);
    }

    @Override
    public String getName() {
        return columnName;
    }

    @Override
    public R process(Map<String, Object> input) {
        return metricFieldProcessor.process(input);
    }

}
