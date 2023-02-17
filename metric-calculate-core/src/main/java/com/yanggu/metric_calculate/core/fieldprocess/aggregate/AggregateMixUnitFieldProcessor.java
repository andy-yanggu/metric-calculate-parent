package com.yanggu.metric_calculate.core.fieldprocess.aggregate;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class AggregateMixUnitFieldProcessor<T, M extends MergedUnit<M>> implements AggregateFieldProcessor<T, M> {

    private MixUnitUdafParam mixUnitUdafParam;

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    private Map<String, BaseAggregateFieldProcessor<T, ?>> multiBaseAggProcessorMap;

    private Expression expression;

    @Override
    public void init() throws Exception {
        Map<String, BaseUdafParam> mixAggMap = mixUnitUdafParam.getMixAggMap();

        Map<String, BaseAggregateFieldProcessor<T, ?>> map = new HashMap<>();
        for (Map.Entry<String, BaseUdafParam> entry : mixAggMap.entrySet()) {
            String paramName = entry.getKey();
            List<BaseUdafParam> baseUdafParamList = Collections.singletonList(entry.getValue());
            BaseAggregateFieldProcessor<T, ?> baseAggregateFieldProcessor =
                    FieldProcessorUtil.getBaseAggregateFieldProcessor(baseUdafParamList, unitFactory, fieldMap);
            map.put(paramName, baseAggregateFieldProcessor);
        }

        this.multiBaseAggProcessorMap = map;

        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        this.expression = instance.compile(mixUnitUdafParam.getExpress(), true);
    }

    @Override
    public M process(T input) throws Exception {
        Map<String, MergedUnit<?>> dataMap = new HashMap<>();
        for (Map.Entry<String, BaseAggregateFieldProcessor<T, ?>> entry : multiBaseAggProcessorMap.entrySet()) {
            String key = entry.getKey();
            MergedUnit<?> process = entry.getValue().process(input);
            if (process != null) {
                dataMap.put(key, process);
            }
        }
        return (M) unitFactory.initInstanceByValue(mixUnitUdafParam.getAggregateType(), dataMap, mixUnitUdafParam.getParam());
    }

    /**
     * 混合类型的进行表达式计算
     *
     * @param input
     * @return
     */
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
