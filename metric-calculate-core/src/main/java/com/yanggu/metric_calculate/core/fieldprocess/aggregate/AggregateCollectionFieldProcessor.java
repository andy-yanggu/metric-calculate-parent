package com.yanggu.metric_calculate.core.fieldprocess.aggregate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.fieldprocess.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.NumberObjectCollectionUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;

import java.util.Map;


/**
 * 聚合集合型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateCollectionFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

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
    private MetricFieldProcessor<?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {

        Collective collective = mergeUnitClazz.getAnnotation(Collective.class);

        Map<String, Class<?>> fieldMap = getFieldMap();
        //设置了去重字段
        if (collective.useDistinctField()) {
            this.multiFieldDistinctFieldProcessor =
                    FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, udafParam.getDistinctFieldList());
        }

        //设置了排序字段
        if (collective.useSortedField()) {
            this.multiFieldOrderFieldProcessor =
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, udafParam.getSortFieldList());
        }

        //设置了保留字段
        if (!collective.retainObject()) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpress());
        }
    }

    @Override
    public M process(JSONObject input) throws Exception {

        Collective collective = mergeUnitClazz.getAnnotation(Collective.class);

        //获取保留字段或者原始数据
        Cloneable2Wrapper<Object> retainFieldValue = getRetainFieldValue(input, collective.retainObject());

        //默认没有去重字段或者排序字段
        Object result = retainFieldValue;
        if (collective.useDistinctField()) {
            MultiFieldDistinctKey distinctKey = multiFieldDistinctFieldProcessor.process(input);
            if (distinctKey == null) {
                return null;
            }
            result = new KeyValue<>(distinctKey, retainFieldValue);
        }

        if (collective.useSortedField()) {
            MultiFieldOrderCompareKey multiFieldOrderCompareKey = multiFieldOrderFieldProcessor.process(input);
            if (multiFieldOrderCompareKey == null) {
                return null;
            }
            result = new KeyValue<>(multiFieldOrderCompareKey, retainFieldValue);
        }

        return (M) unitFactory.initInstanceByValue(aggregateType, result, udafParam.getParam());
    }

}
