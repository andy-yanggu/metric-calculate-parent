package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.pojo.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.MixUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class AggregateMixUnitFieldProcessor<M extends MergedUnit<M>> implements AggregateFieldProcessor<M> {

    private MixUnitUdafParam mixUnitUdafParam;

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    //private Map<String, >

    private Map<String, BaseAggregateFieldProcessor<?>> mixBaseAggregateFieldProcessorMap;

    private Expression expression;

    @Override
    public void init() throws Exception {
        Map<String, BaseUdafParam> mixAggMap = mixUnitUdafParam.getMixAggMap();

        Map<String, BaseAggregateFieldProcessor<?>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            String paramName = entry.getKey();
            BaseUdafParam baseUdafParm = entry.getValue();
            BaseAggregateFieldProcessor<?> baseAggregateFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(baseUdafParm, unitFactory, fieldMap);
            map.put(paramName, baseAggregateFieldProcessor);
        }

        this.mixBaseAggregateFieldProcessorMap = map;

        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        Expression compile = instance.compile(mixUnitUdafParam.getExpress(), true);
        this.expression = compile;
    }

    @Override
    public M process(JSONObject input) throws Exception {
        Map<String, MergedUnit<?>> dataMap = new HashMap<>();
        for (Map.Entry<String, BaseAggregateFieldProcessor<?>> entry : mixBaseAggregateFieldProcessorMap.entrySet()) {
            String key = entry.getKey();
            MergedUnit<?> process = entry.getValue().process(input);
            dataMap.put(key, process);
        }
        return (M) unitFactory.initInstanceByValue(mixUnitUdafParam.getAggregateType(), dataMap, mixUnitUdafParam.getParam());
    }

    @Override
    public String getAggregateType() {
        return mixUnitUdafParam.getAggregateType();
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return unitFactory.getMergeableClass(mixUnitUdafParam.getAggregateType());
    }

}
