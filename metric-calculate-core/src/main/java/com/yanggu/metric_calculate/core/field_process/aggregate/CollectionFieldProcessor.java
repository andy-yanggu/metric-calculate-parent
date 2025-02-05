package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.aggregate_function.annotation.Collective;
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
import org.dromara.hutool.core.lang.tuple.Pair;

import java.util.Map;

/**
 * 集合型字段处理器
 *
 * @param <IN>
 */
@Getter
@EqualsAndHashCode
public class CollectionFieldProcessor<IN> implements FieldProcessor<Map<String, Object>, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final BaseUdafParam udafParam;

    private final Collective collective;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 多字段去重字段处理器
     */
    private MultiFieldDataFieldProcessor multiFieldDataFieldProcessor;

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldDataFieldProcessor multiFieldOrderFieldProcessor;

    /**
     * 保留字段字段处理器
     */
    private MetricFieldProcessor<Object> retainFieldValueFieldProcessor;

    public CollectionFieldProcessor(Map<String, Class<?>> fieldMap,
                                    BaseUdafParam udafParam,
                                    Collective collective,
                                    AviatorFunctionFactory aviatorFunctionFactory) {
        this.fieldMap = fieldMap;
        this.udafParam = udafParam;
        this.collective = collective;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
    }

    @Override
    public void init() throws Exception {
        int keyStrategy = collective.keyStrategy();
        if (keyStrategy != 0 && keyStrategy != 1 && keyStrategy != 2) {
            throw new RuntimeException("主键策略错误: " + keyStrategy);
        }
        //设置了去重字段
        if (keyStrategy == 1) {
            this.multiFieldDataFieldProcessor =
                    FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, udafParam.getMetricExpressParamList(), aviatorFunctionFactory);
            //设置了排序字段
        } else if (keyStrategy == 2) {
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getMultiFieldDataFieldProcessor(fieldMap, udafParam.getMetricExpressParamList(), aviatorFunctionFactory);
        }

        //设置了保留字段
        int retainStrategy = collective.retainStrategy();
        if (retainStrategy != 0 && retainStrategy != 1 && retainStrategy != 2) {
            throw new RuntimeException("保留策略错误: " + retainStrategy);
        }
        if (retainStrategy == 1) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getMetricExpressParam(), aviatorFunctionFactory);
        }
    }

    @SneakyThrows
    @Override
    public IN process(Map<String, Object> input) {
        int keyStrategy = collective.keyStrategy();
        int retainStrategy = collective.retainStrategy();
        Object result = null;
        //使用了去重字段
        if (keyStrategy == 1) {
            MultiFieldData distinctKey = multiFieldDataFieldProcessor.process(input);
            if (distinctKey == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = distinctKey;
            } else if (retainStrategy == 1) {
                result = new Pair<>(distinctKey, retainFieldValueFieldProcessor.process(input));
            } else if (retainStrategy == 2) {
                result = new Pair<>(distinctKey, input);
            }
            //使用了排序字段
        } else if (keyStrategy == 2) {
            MultiFieldData multiFieldData = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldData == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = new Pair<>(multiFieldData, null);
            } else if (retainStrategy == 1) {
                result = new Pair<>(multiFieldData, retainFieldValueFieldProcessor.process(input));
            } else if (retainStrategy == 2) {
                result = new Pair<>(multiFieldData, input);
            }
        } else {
            if (retainStrategy == 0) {
                result = null;
            } else if (retainStrategy == 1) {
                result = retainFieldValueFieldProcessor.process(input);
            } else if (retainStrategy == 2) {
                result = input;
            }
        }
        return (IN) result;
    }

}
