package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.util.KeyValue;
import lombok.Data;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对象型字段处理器
 *
 * @param <IN>
 */
@Data
public class ObjectFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private Objective objective;

    private AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldOrderFieldProcessor multiFieldOrderFieldProcessor;

    /**
     * 保留字段字段处理器
     */
    private MetricFieldProcessor<Object> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {
        int keyStrategy = objective.keyStrategy();
        //如果是设置了比较字段
        if (keyStrategy == 3) {
            if (CollUtil.isEmpty(udafParam.getObjectiveCompareFieldParamList())) {
                throw new RuntimeException("对象型比较字段列表为空");
            }
            List<FieldOrderParam> collect = udafParam.getObjectiveCompareFieldParamList().stream()
                    .map(tempCompareField -> new FieldOrderParam(tempCompareField, true))
                    .collect(Collectors.toList());
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getFieldOrderFieldProcessor(fieldMap, collect, aviatorFunctionFactory);
        }

        int retainStrategy = objective.retainStrategy();
        if (retainStrategy != 0 && retainStrategy != 1 && retainStrategy != 2) {
            throw new RuntimeException("保留策略错误: " + retainStrategy);
        }
        //如果设置了保留字段
        if (retainStrategy == 1) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpressParam(), aviatorFunctionFactory);
        }
    }

    @Override
    @SneakyThrows
    public IN process(JSONObject input) {
        Object result;

        int retainStrategy = objective.retainStrategy();
        //如果使用比较字段
        if (objective.keyStrategy() == 3) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = multiFieldOrderCompareKey;
            } else if (retainStrategy == 1) {
                result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValueFieldProcessor.process(input));
            } else {
                result = new KeyValue<>(multiFieldOrderCompareKey, input);
            }
        } else {
            if (retainStrategy == 0) {
                result = null;
            } else if (retainStrategy == 1) {
                result = retainFieldValueFieldProcessor.process(input);
            } else {
                result = input;
            }
        }
        return (IN) result;
    }

}