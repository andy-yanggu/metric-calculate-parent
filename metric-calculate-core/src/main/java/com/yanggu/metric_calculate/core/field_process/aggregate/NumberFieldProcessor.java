package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Numerical;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field.MultiFieldDataFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 数值型字段提取器
 *
 * @param <IN>
 */
@Getter
@EqualsAndHashCode
public class NumberFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final BaseUdafParam udafParam;

    private final Numerical numerical;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private MetricFieldProcessor<Number> metricFieldProcessor;

    private MultiFieldDataFieldProcessor multiFieldDataFieldProcessor;

    public NumberFieldProcessor(Map<String, Class<?>> fieldMap,
                                BaseUdafParam udafParam,
                                Numerical numerical,
                                AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.udafParam = udafParam;
        this.numerical = numerical;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    public void init() throws Exception {
        if (numerical.multiNumber()) {
            List<AviatorExpressParam> metricExpressList = udafParam.getMetricExpressParamList();
            if (CollUtil.isEmpty(metricExpressList)) {
                throw new RuntimeException("度量字段列表为空");
            }
            this.multiFieldDataFieldProcessor = FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, metricExpressList, aviatorFunctionFactory);
        } else {
            AviatorExpressParam metricExpress = udafParam.getMetricExpressParam();
            if (metricExpress == null || StrUtil.isBlank(metricExpress.getExpress())) {
                throw new RuntimeException("度量字段为空");
            }
            this.metricFieldProcessor = FieldProcessorUtil.getMetricFieldProcessor(fieldMap, metricExpress, aviatorFunctionFactory);
        }
    }

    @Override
    public IN process(JSONObject input) throws Exception {
        Object process;
        if (numerical.multiNumber()) {
            process = multiFieldDataFieldProcessor.process(input);
        } else {
            process = this.metricFieldProcessor.process(input);
        }
        return (IN) process;
    }

}
