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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Data
public class AggregateMixUnitFieldProcessor<M extends MergedUnit<M>> implements AggregateFieldProcessor<M> {

    private MixUnitUdafParam mixUnitUdafParam;

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    private Map<String, BaseAggregateFieldProcessor<?>> multiBaseAggProcessorMap;

    private Expression expression;

    @Override
    public void init() throws Exception {
        Map<String, BaseUdafParam> mixAggMap = mixUnitUdafParam.getMixAggMap();

        Map<String, BaseAggregateFieldProcessor<?>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            String paramName = entry.getKey();
            BaseUdafParam baseUdafParam = entry.getValue();
            BaseAggregateFieldProcessor<?> baseAggregateFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(Collections.singletonList(baseUdafParam), unitFactory, fieldMap);
            map.put(paramName, baseAggregateFieldProcessor);
        }

        this.multiBaseAggProcessorMap = map;

        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        this.expression = instance.compile(mixUnitUdafParam.getExpress(), true);
    }

    @Override
    public M process(JSONObject input) throws Exception {
        Map<String, MergedUnit<?>> dataMap = new HashMap<>();
        for (Map.Entry<String, BaseAggregateFieldProcessor<?>> entry : multiBaseAggProcessorMap.entrySet()) {
            String key = entry.getKey();
            MergedUnit<?> process = entry.getValue().process(input);
            if (process != null) {
                dataMap.put(key, process);
            }
        }
        return (M) unitFactory.initInstanceByValue(mixUnitUdafParam.getAggregateType(), dataMap, mixUnitUdafParam.getParam());
    }

    @Override
    public Object callBack(Object input) {
        return expression.execute((Map<String, Object>) input);
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
