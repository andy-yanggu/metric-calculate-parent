package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import com.yanggu.metric_calculate.core2.aggregate_function.annotation.Collective;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * 集合型字段处理器
 *
 * @param <IN>
 */
@Data
public class CollectionFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private Collective collective;

    private AviatorFunctionFactory aviatorFunctionFactory;

    /**
     * 多字段去重字段处理器
     */
    private MultiFieldDistinctFieldProcessor multiFieldDistinctFieldProcessor;

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
        int keyStrategy = collective.keyStrategy();
        //设置了去重字段
        if (keyStrategy == 1) {
            this.multiFieldDistinctFieldProcessor =
                    FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, udafParam.getDistinctFieldListParamList(), aviatorFunctionFactory);
            //设置了排序字段
        } else if (keyStrategy == 2) {
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, udafParam.getCollectiveSortFieldList(), aviatorFunctionFactory);
        }

        //设置了保留字段
        int retainStrategy = collective.retainStrategy();
        if (retainStrategy != 0 && retainStrategy != 1 && retainStrategy != 2) {
            throw new RuntimeException("保留策略错误: " + retainStrategy);
        }
        if (retainStrategy == 1) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpressParam(), aviatorFunctionFactory);
        }
    }

    @SneakyThrows
    @Override
    public IN process(JSONObject input) {
        int keyStrategy = collective.keyStrategy();
        int retainStrategy = collective.retainStrategy();
        Object result = null;
        //使用了去重字段
        if (keyStrategy == 1) {
            MultiFieldDistinctKey distinctKey = multiFieldDistinctFieldProcessor.process(input);
            if (distinctKey == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = distinctKey;
            } else if (retainStrategy == 1) {
                result = new KeyValue<>(distinctKey, retainFieldValueFieldProcessor.process(input));
            } else if (retainStrategy == 2) {
                result = new KeyValue<>(distinctKey, input);
            }
            //使用了排序字段
        } else if (keyStrategy == 2) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            if (retainStrategy == 0) {
                result = multiFieldOrderCompareKey;
            } else if (retainStrategy == 1) {
                result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValueFieldProcessor.process(input));
            } else if (retainStrategy == 2) {
                result = new KeyValue<>(multiFieldOrderCompareKey, input);
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
