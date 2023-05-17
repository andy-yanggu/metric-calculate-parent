package com.yanggu.metric_calculate.core2.field_process.aggregate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core2.util.FieldProcessorUtil;
import lombok.Data;

import java.util.Map;

/**
 * 映射型字段处理器
 */
@Data
public class MapFieldProcessor<IN> implements FieldProcessor<JSONObject, IN> {

    private MapUdafParam mapUdafParam;

    private Map<String, Class<?>> fieldMap;

    private AggregateFunctionFactory aggregateFunctionFactory;

    /**
     * key生成字段处理器
     */
    private MultiFieldDistinctFieldProcessor keyFieldProcessor;

    /**
     * value生成字段处理器
     */
    private FieldProcessor<JSONObject, Object> valueAggregateFieldProcessor;

    @Override
    public void init() throws Exception {

        //检查相关参数是否设置
        check();

        //map的key字段处理器
        this.keyFieldProcessor =
                FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, mapUdafParam.getDistinctFieldList());

        //map的value字段处理器
        BaseUdafParam valueAggParam = mapUdafParam.getValueAggParam();
        AggregateFunction<Object, Object, Object> aggregateFunction =
                aggregateFunctionFactory.getAggregateFunction(valueAggParam.getAggregateType());
        this.valueAggregateFieldProcessor =
                FieldProcessorUtil.getBaseFieldProcessor(valueAggParam, fieldMap, aggregateFunction);
    }

    @Override
    public IN process(JSONObject input) throws Exception {
        MultiFieldDistinctKey key = keyFieldProcessor.process(input);
        Object value = valueAggregateFieldProcessor.process(input);
        return (IN) Pair.of(key, value);
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

        if (CollUtil.isEmpty(mapUdafParam.getDistinctFieldList())) {
            throw new RuntimeException("去重字段列表为空");
        }
    }

}