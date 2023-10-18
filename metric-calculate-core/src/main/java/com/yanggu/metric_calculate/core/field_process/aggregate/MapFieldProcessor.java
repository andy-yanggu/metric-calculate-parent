package com.yanggu.metric_calculate.core.field_process.aggregate;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.multi_field.MultiFieldFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.json.JSONObject;

import java.util.Map;

/**
 * 映射型字段处理器
 */
@Getter
@EqualsAndHashCode
public class MapFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private final Map<String, Class<?>> fieldMap;

    private final MapUdafParam mapUdafParam;

    private final AviatorFunctionFactory aviatorFunctionFactory;

    private final AggregateFunctionFactory aggregateFunctionFactory;

    /**
     * key生成字段处理器
     */
    private MultiFieldFieldProcessor keyFieldProcessor;

    /**
     * value生成字段处理器
     */
    private FieldProcessor<JSONObject, Object> valueAggregateFieldProcessor;

    public MapFieldProcessor(Map<String, Class<?>> fieldMap,
                             MapUdafParam mapUdafParam,
                             AviatorFunctionFactory aviatorFunctionFactory,
                             AggregateFunctionFactory aggregateFunctionFactory) {
        this.fieldMap = fieldMap;
        this.mapUdafParam = mapUdafParam;
        this.aviatorFunctionFactory = aviatorFunctionFactory;
        this.aggregateFunctionFactory = aggregateFunctionFactory;
    }

    @Override
    public void init() throws Exception {

        //检查相关参数是否设置
        check();

        //map的key字段处理器
        this.keyFieldProcessor =
                FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, mapUdafParam.getDistinctFieldParamList(), aviatorFunctionFactory);

        //map的value字段处理器
        this.valueAggregateFieldProcessor =
                FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, mapUdafParam.getValueAggParam(), aviatorFunctionFactory, aggregateFunctionFactory);
    }

    @Override
    public IN process(JSONObject input) throws Exception {
        MultiFieldData key = keyFieldProcessor.process(input);
        Object value = valueAggregateFieldProcessor.process(input);
        return (IN) new Pair<>(key, value);
    }

    private void check() {
        if (mapUdafParam == null) {
            throw new RuntimeException("映射类型udaf参数为空");
        }

        if (mapUdafParam.getValueAggParam() == null) {
            throw new RuntimeException("value的聚合函数参数为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        if (CollUtil.isEmpty(mapUdafParam.getDistinctFieldParamList())) {
            throw new RuntimeException("去重字段列表为空");
        }
    }

}