package com.yanggu.metric_calculate.core.fieldprocess.aggregate;


import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.pojo.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.Map;


/**
 * 映射型字段处理器
 *
 */
@Data
public class AggregateMapUnitFieldProcessor implements FieldProcessor<JSONObject, MergedUnit<?>> {

    private MapUnitUdafParam mapUnitUdafParam;

    private String aggregateType;

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    /**
     * key生成字段处理器
     */
    private MultiFieldDistinctFieldProcessor keyFieldProcessor;

    private BaseAggregateFieldProcessor<?> valueAggregateFieldProcessor;

    @Override
    public void init() throws Exception {
        //map的key字段处理器
        this.keyFieldProcessor =
                FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, mapUnitUdafParam.getDistinctFieldList());


        //map的value字段处理器
        this.valueAggregateFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(mapUnitUdafParam.getValueAggParam(), unitFactory,
                                fieldMap, mapUnitUdafParam.getValueAggregateType());
    }

    @Override
    public MergedUnit<?> process(JSONObject input) throws Exception {
        MultiFieldDistinctKey key = keyFieldProcessor.process(input);

        MergedUnit<?> value = valueAggregateFieldProcessor.process(input);
        Tuple tuple = new Tuple(key, value);
        return unitFactory.initInstanceByValue(aggregateType, tuple, mapUnitUdafParam.getParam());
    }

}
