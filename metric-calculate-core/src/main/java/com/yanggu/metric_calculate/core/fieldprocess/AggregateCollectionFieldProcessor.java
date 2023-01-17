package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

//TODO 增加kryo序列化器
/**
 * 聚合集合型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateCollectionFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    private MetricFieldProcessor<?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {

        //如果是设置了比较字段
        Collective objective = mergeUnitClazz.getAnnotation(Collective.class);
        if (objective.useCompareField()) {
            super.init();
        }

        //如果设置了保留字段
        if (!objective.retainObject() && retainFieldValueFieldProcessor == null) {
            throw new RuntimeException("保留字段处理器为空");
        }
    }

    @Override
    public M process(JSONObject input) throws Exception {

        Collective objective = mergeUnitClazz.getAnnotation(Collective.class);

        //是否使用比较字段
        boolean useCompareField = objective.useCompareField();

        //是否保留对象
        boolean retainObject = objective.retainObject();

        //使用比较字段
        Object result;
        if (useCompareField) {
            //获取比较值
            Object compareFieldValue = super.process(input);
            if (compareFieldValue == null) {
                return null;
            }

            //获取保留值
            Object value = getValue(input, retainObject);
            if (value == null) {
                return null;
            }
            result = new KeyValue<>((Comparable<?>) compareFieldValue, value);
        } else {
            //没有比较字段, 直接获取保留值
            Object value = getValue(input, retainObject);
            if (value == null) {
                return null;
            }
            result = Cloneable2Wrapper.wrap(value);
        }
        test(udafParams);
        return (M) unitFactory.initInstanceByValue(aggregateType, result, udafParams);
    }

    private Object getValue(JSONObject input, boolean retainObject) throws Exception {
        Object value;
        if (retainObject) {
            value = input;
        } else {
            Object retainField = retainFieldValueFieldProcessor.process(input);
            if (retainField == null) {
                return null;
            }
            value = retainField;
        }
        return value;
    }

    @SneakyThrows
    private void test(Map<String, Object> udafParams) {
        if (CollUtil.isEmpty(udafParams)) {
            return;
        }

        //如果是计数窗口, 需要添加聚合字段处理器
        MergeType annotation = mergeUnitClazz.getAnnotation(MergeType.class);
        if (!annotation.countWindow()) {
            return;
        }

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor;

        String aggregateType = udafParams.get("aggregateType").toString();

        Map<String, Class<?>> fieldMap = getFieldMap();
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(aggregateType);
        if (mergeUnitClazz.isAnnotationPresent(Numerical.class)) {
            aggregateFieldProcessor = new AggregateNumberFieldProcessor<>();
            aggregateFieldProcessor.setMetricExpress(udafParams.get("metricExpress").toString());
        } else if (mergeUnitClazz.isAnnotationPresent(Objective.class)) {
            aggregateFieldProcessor = new AggregateObjectFieldProcessor<>();
            //设置保留字段处理器
            Objective objective = mergeUnitClazz.getAnnotation(Objective.class);
            if (objective.useCompareField()) {
                aggregateFieldProcessor.setMetricExpress(udafParams.get("metricExpress").toString());
            }
            boolean retainObject = objective.retainObject();
            if (!retainObject) {
                MetricFieldProcessor<?> tempRetainFieldValueFieldProcessor = new MetricFieldProcessor<>();
                tempRetainFieldValueFieldProcessor.setMetricExpress(udafParams.get("retainExpress").toString());
                tempRetainFieldValueFieldProcessor.setFieldMap(fieldMap);
                tempRetainFieldValueFieldProcessor.init();

                ((AggregateObjectFieldProcessor<?>) aggregateFieldProcessor)
                        .setRetainFieldValueFieldProcessor(tempRetainFieldValueFieldProcessor);
            }
        } else if (mergeUnitClazz.isAnnotationPresent(Collective.class)) {
            aggregateFieldProcessor = new AggregateCollectionFieldProcessor<>();
            //设置保留字段处理器
            Collective collective = mergeUnitClazz.getAnnotation(Collective.class);
            if (collective.useCompareField()) {
                aggregateFieldProcessor.setMetricExpress(udafParams.get("metricExpress").toString());
            }
            boolean retainObject = collective.retainObject();
            if (!retainObject) {
                MetricFieldProcessor<?> tempRetainFieldValueFieldProcessor = new MetricFieldProcessor<>();
                tempRetainFieldValueFieldProcessor.setMetricExpress(udafParams.get("retainExpress").toString());
                tempRetainFieldValueFieldProcessor.setFieldMap(fieldMap);
                tempRetainFieldValueFieldProcessor.init();

                ((AggregateCollectionFieldProcessor<?>) aggregateFieldProcessor)
                        .setRetainFieldValueFieldProcessor(tempRetainFieldValueFieldProcessor);
            }
        } else {
            throw new RuntimeException("不支持的聚合类型: " + aggregateType);
        }

        //聚合字段处理器
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(aggregateType);
        aggregateFieldProcessor.setUdafParams(((Map<String, Object>) udafParams.get("udafParams")));
        aggregateFieldProcessor.setUnitFactory(unitFactory);
        aggregateFieldProcessor.setMergeUnitClazz(mergeUnitClazz);
        aggregateFieldProcessor.init();
        udafParams.put("aggregateFieldProcessor", aggregateFieldProcessor);
    }

}
