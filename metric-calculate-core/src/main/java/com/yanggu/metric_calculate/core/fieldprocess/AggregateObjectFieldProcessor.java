package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import lombok.Data;

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

        return super.getResult(useCompareField, retainObject, input);
    }

}
