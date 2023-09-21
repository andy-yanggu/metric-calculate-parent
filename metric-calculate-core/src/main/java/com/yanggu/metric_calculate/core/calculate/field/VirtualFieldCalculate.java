package com.yanggu.metric_calculate.core.calculate.field;

import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;
import org.dromara.hutool.json.JSONObject;

import java.util.Map;

/**
 * 虚拟字段计算类
 * <p>有些字段是虚拟字段通过其他字段计算得到</p>
 * <p>常见的根据时间戳，得到年、月、日、小时数等</p>
 * <p>可以实现简单的字段补全逻辑</p>
 */
@Data
public class VirtualFieldCalculate<R> implements FieldCalculate<JSONObject, R> {

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
    public String getName() {
        return columnName;
    }

    @Override
    public R process(JSONObject input) {
        return metricFieldProcessor.process(input);
    }

}
