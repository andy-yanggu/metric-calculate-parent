package com.yanggu.metric_calculate.core2.util;


import com.yanggu.metric_calculate.core2.field_process.aggregate.AbstractAggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.NumberAggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Dimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeColumn;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUnitUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 字段处理器工具类
 */
public class FieldProcessorUtil {

    private FieldProcessorUtil() {
    }

    /**
     * 生成前置过滤条件字段字段处理器
     *
     * @param fieldMap      宽表字段
     * @param filterExpress 过滤表达式
     * @return 前置过滤条件字段处理器
     */
    @SneakyThrows
    public static FilterFieldProcessor getFilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                                               String filterExpress) {
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, filterExpress);
        filterFieldProcessor.init();
        return filterFieldProcessor;
    }

    /**
     * 生成时间字段处理器
     *
     * @param timeColumn 时间字段(字段字段名和时间格式)
     * @return 时间字段处理器
     */
    public static TimeFieldProcessor getTimeFieldProcessor(TimeColumn timeColumn) {
        TimeFieldProcessor timeFieldProcessor =
                new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
        timeFieldProcessor.init();
        return timeFieldProcessor;
    }

    /**
     * 生成维度字段处理器
     *
     * @param key           指标唯一标识
     * @param metricName    指标名称
     * @param fieldMap      宽表字段
     * @param dimensionList 维度列表
     * @return 维度字段处理器
     */
    public static DimensionSetProcessor getDimensionSetProcessor(String key,
                                                                 String metricName,
                                                                 Map<String, Class<?>> fieldMap,
                                                                 List<Dimension> dimensionList) {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(dimensionList);
        dimensionSetProcessor.setKey(key);
        dimensionSetProcessor.setMetricName(metricName);
        dimensionSetProcessor.setFieldMap(fieldMap);
        dimensionSetProcessor.init();
        return dimensionSetProcessor;
    }

    /**
     * 生成度量值字段处理器
     *
     * @param fieldMap 宽表字段
     * @param metricExpress 度量表达式
     * @return 度量值字段处理器
     */
    @SneakyThrows
    public static <R> MetricFieldProcessor<R> getMetricFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                      String metricExpress) {
        MetricFieldProcessor<R> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setFieldMap(fieldMap);
        metricFieldProcessor.setMetricExpress(metricExpress);
        metricFieldProcessor.init();
        return metricFieldProcessor;
    }

    /**
     * 生成多字段去重字段处理器
     *
     * @param fieldMap 宽表字段
     * @param distinctFieldList 维度表达式列表
     * @return 多字段去重字段处理器
     */
    @SneakyThrows
    public static MultiFieldDistinctFieldProcessor getDistinctFieldFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                                  List<String> distinctFieldList) {
        MultiFieldDistinctFieldProcessor tempMultiFieldDistinctFieldProcessor = new MultiFieldDistinctFieldProcessor();
        tempMultiFieldDistinctFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldDistinctFieldProcessor.setDistinctFieldList(distinctFieldList);
        tempMultiFieldDistinctFieldProcessor.init();
        return tempMultiFieldDistinctFieldProcessor;
    }

    /**
     * 生成多字段排序字段处理器
     *
     * @param fieldMap 宽表字段
     * @param fieldOrderParamList 多字段排序列表
     * @return 多字段排序字段处理器
     */
    @SneakyThrows
    public static MultiFieldOrderFieldProcessor getOrderFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       List<FieldOrderParam> fieldOrderParamList) {
        MultiFieldOrderFieldProcessor tempMultiFieldOrderFieldProcessor = new MultiFieldOrderFieldProcessor();
        tempMultiFieldOrderFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldOrderFieldProcessor.setFieldOrderParamList(fieldOrderParamList);
        tempMultiFieldOrderFieldProcessor.init();
        return tempMultiFieldOrderFieldProcessor;
    }

    @SneakyThrows
    public static <IN, ACC, OUT> AbstractAggregateFieldProcessor<IN, ACC, OUT> getAbstractAggregateFieldProcessor(
                                                                         Derive derive,
                                                                         Map<String, Class<?>> fieldMap) {
        List<BaseUdafParam> baseUdafParamList = Arrays.asList(derive.getBaseUdafParam(), derive.getExternalBaseUdafParam());
        MapUnitUdafParam mapUdafParam = derive.getMapUdafParam();
        MixUnitUdafParam mixUnitUdafParam = derive.getMixUnitUdafParam();
        String aggregateType = derive.getCalculateLogic();
        AggregateFunction<IN, ACC, OUT> aggregateFunction = AggregateFunctionFactory.getAggregateFunction(aggregateType, null);
        NumberAggregateFieldProcessor<IN, ACC, OUT> numberAggregateFieldProcessor = new NumberAggregateFieldProcessor<>(aggregateFunction);
        numberAggregateFieldProcessor.setUdafParam(derive.getBaseUdafParam());
        numberAggregateFieldProcessor.setFieldMap(fieldMap);
        numberAggregateFieldProcessor.init();
        return numberAggregateFieldProcessor;
    }

}
