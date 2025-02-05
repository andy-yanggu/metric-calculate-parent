package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Objective;
import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field.MultiFieldDataFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 对象型字段处理器
 *
 * @param <IN>
 */
@Getter
@EqualsAndHashCode
public class ObjectFieldProcessor<IN> implements FieldProcessor<Map<String, Object>, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final BaseUdafParam udafParam;

    private final Objective objective;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldDataFieldProcessor multiFieldOrderFieldProcessor;

    /**
     * 保留字段字段处理器
     */
    private MetricFieldProcessor<Object> retainFieldValueFieldProcessor;

    public ObjectFieldProcessor(Map<String, Class<?>> fieldMap,
                                BaseUdafParam udafParam,
                                Objective objective,
                                AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.udafParam = udafParam;
        this.objective = objective;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    public void init() throws Exception {
        int keyStrategy = objective.keyStrategy();
        //如果是设置了比较字段
        if (keyStrategy == 3) {
            if (CollUtil.isEmpty(udafParam.getMetricExpressParamList())) {
                throw new RuntimeException("对象型比较字段列表为空");
            }
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, udafParam.getMetricExpressParamList(), aviatorFunctionFactory);
            Map<String, Object> param = udafParam.getParam();
            if (MapUtil.isEmpty(param)) {
                param = new HashMap<>();
                udafParam.setParam(param);
            }
            //放入比较对象的长度
            param.put("compareParamLength", udafParam.getMetricExpressParamList().size());
        }

        int retainStrategy = objective.retainStrategy();
        if (retainStrategy != 0 && retainStrategy != 1 && retainStrategy != 2) {
            throw new RuntimeException("保留策略错误: " + retainStrategy);
        }
        //如果设置了保留字段
        if (retainStrategy == 1) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getMetricExpressParam(), aviatorFunctionFactory);
        }
    }

    @Override
    @SneakyThrows
    public IN process(Map<String, Object> input) {
        Object result;

        int retainStrategy = objective.retainStrategy();
        //如果使用比较字段
        if (objective.keyStrategy() == 3) {
            MultiFieldData multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = new Pair<>(multiFieldOrderCompareKey, null);
            } else if (retainStrategy == 1) {
                result = new Pair<>(multiFieldOrderCompareKey, retainFieldValueFieldProcessor.process(input));
            } else {
                result = new Pair<>(multiFieldOrderCompareKey, input);
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