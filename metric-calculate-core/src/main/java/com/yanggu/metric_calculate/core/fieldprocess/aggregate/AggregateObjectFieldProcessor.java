package com.yanggu.metric_calculate.core.fieldprocess.aggregate;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
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
public class AggregateObjectFieldProcessor<T, M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<T, M> {

    /**
     * 多字段排序字段处理器
     */
    private MultiFieldOrderFieldProcessor<T> multiFieldOrderFieldProcessor;

    /**
     * 保留字段字段处理器
     */
    private MetricFieldProcessor<T, ?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {
        super.init();

        Objective objective = mergeUnitClazz.getAnnotation(Objective.class);

        Map<String, Class<?>> fieldMap = getFieldMap();
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
    public M process(T input) {

        Objective objective = mergeUnitClazz.getAnnotation(Objective.class);

        //获取保留字段或者原始数据
        Cloneable2Wrapper<Object> retainFieldValue = getRetainFieldValue(input, objective.retainObject());

        //默认没有排序字段
        Object result = retainFieldValue;

        if (objective.useCompareField()) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValue);
        }
        return (M) unitFactory.initInstanceByValue(aggregateType, result, udafParam.getParam());
    }

}
