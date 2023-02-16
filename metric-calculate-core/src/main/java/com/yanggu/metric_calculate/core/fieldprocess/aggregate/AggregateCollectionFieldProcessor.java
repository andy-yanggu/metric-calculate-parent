package com.yanggu.metric_calculate.core.fieldprocess.aggregate;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.BaseUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.ValueMapper;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 聚合集合型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateCollectionFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    /**
     * 对于滑动计数窗口和CEP类型, 需要额外的聚合处理器
     */
    private BaseUdafParam externalBaseUdafParam;

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

    /**
     * 需要进行二次聚合计算
     * <p>例如滑动计数窗口函数, 最近5次, 求平均值</p>
     * <p>CEP, 按照最后一条数据进行聚合计算</p>
     */
    private BaseAggregateFieldProcessor<?> externalAggregateFieldProcessor;

    @Override
    public void init() throws Exception {
        super.init();

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
                    FieldProcessorUtil.getOrderFieldProcessor(fieldMap, udafParam.getCollectiveSortFieldList());
        }

        //设置了保留字段
        if (!collective.retainObject()) {
            this.retainFieldValueFieldProcessor =
                    FieldProcessorUtil.getMetricFieldProcessor(fieldMap, udafParam.getRetainExpress());
        }

        //判断是否需要额外聚合处理器
        if (getMergeUnitClazz().getAnnotation(MergeType.class).useExternalAgg()) {
            this.externalAggregateFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(
                            Collections.singletonList(externalBaseUdafParam), unitFactory, fieldMap);
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

    @Override
    public Object callBack(Object input) {
        MergeType annotation = getMergeUnitClazz().getAnnotation(MergeType.class);
        if (!annotation.useExternalAgg() || !(input instanceof List)) {
            return input;
        }
        List<JSONObject> tempValueList = (List<JSONObject>) input;
        MergedUnit mergedUnit = tempValueList.stream()
                .map(tempValue -> {
                    try {
                        return (MergedUnit) externalAggregateFieldProcessor.process(tempValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce(MergedUnit::merge)
                .orElseThrow(() -> new RuntimeException("MergeUnit的merge方法执行失败"));
        return ValueMapper.value(((Value<?>) mergedUnit));
    }

}
