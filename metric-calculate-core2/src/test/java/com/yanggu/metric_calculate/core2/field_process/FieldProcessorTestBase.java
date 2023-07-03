package com.yanggu.metric_calculate.core2.field_process;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.MapFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.MixFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUdafParam;

import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactoryBase.getAggregateFunctionFactory;
import static com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactoryTest.getAviatorFunctionFactory;

public class FieldProcessorTestBase {

    public static final AviatorFunctionFactory AVIATOR_FUNCTION_FACTORY = getAviatorFunctionFactory();

    public static final AggregateFunctionFactory AGGREGATE_FUNCTION_FACTORY = getAggregateFunctionFactory();

    public static FilterFieldProcessor getFilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                                               AviatorExpressParam filterExpressParam) {
        return FieldProcessorUtil.getFilterFieldProcessor(fieldMap, filterExpressParam, AVIATOR_FUNCTION_FACTORY);
    }

    public static MetricListFieldProcessor getMetricListFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       List<AviatorExpressParam> metricExpressParamList) {
        return FieldProcessorUtil.getMetricListFieldProcessor(fieldMap, metricExpressParamList, AVIATOR_FUNCTION_FACTORY);
    }

    public static MultiFieldDistinctFieldProcessor getDistinctFieldFieldProcessor(
                                                                Map<String, Class<?>> fieldMap,
                                                                List<AviatorExpressParam> distinctFieldListParamList) {
        return FieldProcessorUtil.getDistinctFieldFieldProcessor(fieldMap, distinctFieldListParamList, AVIATOR_FUNCTION_FACTORY);
    }

    public static MultiFieldOrderFieldProcessor getFieldOrderFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                            List<FieldOrderParam> fieldOrderParamList) {
        return FieldProcessorUtil.getFieldOrderFieldProcessor(fieldMap, fieldOrderParamList, AVIATOR_FUNCTION_FACTORY);
    }

    public static <T> FieldProcessor<JSONObject, T> getBaseAggregateFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                                   BaseUdafParam baseUdafParam) {
        return FieldProcessorUtil.getBaseAggregateFieldProcessor(fieldMap, baseUdafParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);
    }

    public static <T> MapFieldProcessor<T> getMapFieldProcessor(Map<String, Class<?>> fieldMap, MapUdafParam mapUdafParam) {
        return FieldProcessorUtil.getMapFieldProcessor(fieldMap, mapUdafParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);
    }

    public static <T> MixFieldProcessor<T> getMixFieldProcessor(Map<String, Class<?>> fieldMap, MixUdafParam mixUdafParam) {
        return FieldProcessorUtil.getMixFieldProcessor(fieldMap, mixUdafParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);
    }

    public static <IN, ACC, OUT> AggregateFieldProcessor<IN, ACC, OUT> getAggregateFieldProcessor(
                                                                      Map<String, Class<?>> fieldMap,
                                                                      AggregateFunctionParam aggregateFunctionParam,
                                                                      AggregateFunctionFactory aggregateFunctionFactory) {
        return FieldProcessorUtil.getAggregateFieldProcessor(fieldMap, aggregateFunctionParam, AVIATOR_FUNCTION_FACTORY, aggregateFunctionFactory);
    }

    public static <IN, ACC, OUT> AggregateFieldProcessor<IN, ACC, OUT> getAggregateFieldProcessor(
                                                                      Map<String, Class<?>> fieldMap,
                                                                      AggregateFunctionParam aggregateFunctionParam) {
        return FieldProcessorUtil.getAggregateFieldProcessor(fieldMap, aggregateFunctionParam, AVIATOR_FUNCTION_FACTORY, AGGREGATE_FUNCTION_FACTORY);
    }

}
