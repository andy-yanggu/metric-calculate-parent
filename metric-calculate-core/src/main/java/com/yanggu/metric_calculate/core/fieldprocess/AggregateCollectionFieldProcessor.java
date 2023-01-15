package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;


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

}
