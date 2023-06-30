package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import com.yanggu.metric_calculate.core2.annotation.Objective;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;
import lombok.SneakyThrows;

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
            if (CollUtil.isEmpty(udafParam.getObjectiveCompareFieldList())) {
                throw new RuntimeException("对象型比较字段列表为空");
            }
            List<FieldOrderParam> collect = udafParam.getCollectiveSortFieldList().stream()
                    .map(tempCompareField -> new FieldOrderParam(tempCompareField.getAviatorExpressParam(), true))
                    .collect(Collectors.toList());
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, collect);
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