package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.KeyValue;
import lombok.Data;

//TODO 增加kryo序列化器
/**
 * 聚合对象型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateObjectFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    private MetricFieldProcessor<?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {

        //如果是设置了比较字段
        Objective objective = mergeUnitClazz.getAnnotation(Objective.class);
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

        Objective objective = mergeUnitClazz.getAnnotation(Objective.class);

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

        //对于集合型
        //1. 一些类型需要指定比较的字段
        //2. 需要指定保留对象还是指定字段
        //3. 对于比较类型对象型, 是否按照key-value的形式保留数据
        //4. 比较相等时, 取之前的数据还是之后的数据。
        //   例如MaxObject, 按照amount进行比较, 两次数据amount相等, 这时候是否更新对象的值
        //   这个配置可以放在udafParams中

        //1. 没有比较字段, 且保留对象, 传入input即可。例如OCCUPIEDOBJECT和REPLACEDOBJECT
        //2. 没有比较字段, 保留指定字段, 需要配置保留字段。例如OCCUPIEDFIELD和REPLACEDFIELD
        //3. 有比较字段, 且保留对象, 需要配置比较字段, 配置是否按照key-value的形式保留数据(配置在udafParams中)
        //      例如MAXOBJECT和MINOBJECT。在udafParams中配置是否按照key-value的形式保留数据, 配置相等时是否更新
        //4. 有比较字段, 且保留指定字段, 需要配置比较字段, 配置指标字段, 配置是否按照key-value的形式保留数据(配置在udafParams中)
        //      例如MAXFIELD和MINOFIELD。在udafParams中配置是否按照key-value的形式保留数据, 配置相等时是否更新
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
