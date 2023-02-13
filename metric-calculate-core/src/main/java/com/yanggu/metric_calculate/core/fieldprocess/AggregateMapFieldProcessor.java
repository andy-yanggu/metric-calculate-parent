package com.yanggu.metric_calculate.core.fieldprocess;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import lombok.Data;


/**
 * 聚合集合型字段处理器
 *
 * @param <M>
 */
@Data
public class AggregateMapFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    private MetricFieldProcessor<?> retainFieldValueFieldProcessor;

    @Override
    public void init() throws Exception {

        //如果是设置了比较字段
        MapType mapType = mergeUnitClazz.getAnnotation(MapType.class);
        if (mapType.useCompareField()) {
            //初始化
            super.init();
        }

        //如果设置了保留字段
        if (!mapType.retainObject() && retainFieldValueFieldProcessor == null) {
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

        return super.getResult(useCompareField, retainObject, input);
    }

}
