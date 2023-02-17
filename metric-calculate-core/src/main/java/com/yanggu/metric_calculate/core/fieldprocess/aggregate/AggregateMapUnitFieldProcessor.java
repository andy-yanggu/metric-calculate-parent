package com.yanggu.metric_calculate.core.fieldprocess.aggregate;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import lombok.Data;

import java.util.Collections;
import java.util.Map;


/**
 * 映射型字段处理器
 *
 */
@Data
public class AggregateMapUnitFieldProcessor<T, M extends MergedUnit<M>> implements AggregateFieldProcessor<T, M> {

    private MapUnitUdafParam mapUnitUdafParam;

    private String aggregateType;

    private Class<? extends MergedUnit<?>> mergeUnitClazz;

    private Map<String, Class<?>> fieldMap;

    private UnitFactory unitFactory;

    /**
     * key生成字段处理器
     */
    private MultiFieldDistinctFieldProcessor<T> keyFieldProcessor;

    /**
     * value生成字段处理器
     */
    private BaseAggregateFieldProcessor<T, ?> valueAggregateFieldProcessor;

    @Override
    public void init() throws Exception {

        //检查相关参数是否设置
        check();

        //map的key字段处理器
        this.keyFieldProcessor =
                FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, mapUnitUdafParam.getDistinctFieldList());

        //map的value字段处理器
        this.valueAggregateFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(
                        Collections.singletonList(mapUnitUdafParam.getValueAggParam()), unitFactory, fieldMap);
    }

    @Override
    public M process(T input) throws Exception {
        MultiFieldDistinctKey key = keyFieldProcessor.process(input);

        MergedUnit<?> value = valueAggregateFieldProcessor.process(input);
        Tuple tuple = new Tuple(key, value);
        return (M) unitFactory.initInstanceByValue(mapUnitUdafParam.getAggregateType(), tuple, mapUnitUdafParam.getParam());
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return mergeUnitClazz;
    }

    private void check() {
        if (mapUnitUdafParam == null) {
            throw new RuntimeException("映射类型udaf参数为空");
        }

        if (mapUnitUdafParam.getValueAggParam() == null) {
            throw new RuntimeException("value的聚合函数参数为空");
        }

        if (StrUtil.isBlank(aggregateType)) {
            throw new RuntimeException("聚合类型为空");
        }

        if (mergeUnitClazz == null) {
            throw new RuntimeException("MergeUnitClazz为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        if (unitFactory == null) {
            throw new RuntimeException("UnitFactory为空");
        }

        if (CollUtil.isEmpty(mapUnitUdafParam.getDistinctFieldList())) {
            throw new RuntimeException("去重字段列表为空");
        }
    }

}
