package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
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
 * 聚合对象型字段处理器
 *
 * @param <M>
 */
@Data
public class ObjectFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private BaseUdafParam udafParam;

    private Map<String, Class<?>> fieldMap;

    private Objective objective;

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
        //如果是设置了比较字段
        if (objective.useCompareField()) {
            if (CollUtil.isEmpty(udafParam.getObjectiveCompareFieldList())) {
                throw new RuntimeException("对象型比较字段列表为空");
            }
            List<FieldOrderParam> collect = udafParam.getObjectiveCompareFieldList().stream()
                    .map(tempCompareField -> new FieldOrderParam(tempCompareField, true))
                    .collect(Collectors.toList());
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, collect);
        }

        //如果设置了保留字段
        if (!objective.retainObject()) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpress());
        }
    }

    @Override
    @SneakyThrows
    public IN process(JSONObject input) {
        //获取保留字段或者原始数据
        Object retainFieldValue = input;
        if (!objective.retainObject()) {
            retainFieldValue = retainFieldValueFieldProcessor.process(input);
        }

        //默认没有排序字段
        Object result = retainFieldValue;

        if (objective.useCompareField()) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValue);
        }
        return (IN) result;
    }

}