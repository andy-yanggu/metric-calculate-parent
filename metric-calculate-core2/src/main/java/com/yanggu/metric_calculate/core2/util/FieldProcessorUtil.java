package com.yanggu.metric_calculate.core2.util;


import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.JavaMethodReflectionFunctionMissing;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.aggregate_function.map.AbstractMapAggregateFunction;
import com.yanggu.metric_calculate.core2.aggregate_function.mix.AbstractMixAggregateFunction;
import com.yanggu.metric_calculate.core2.annotation.*;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.aggregate.*;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Dimension;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeColumn;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUdafParam;
import lombok.SneakyThrows;

import java.util.HashMap;
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
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
        timeFieldProcessor.init();
        return timeFieldProcessor;
    }

    /**
     * 生成维度字段处理器
     *
     * @param key           指标唯一标识
     * @param metricName    指标名称
     * @param dimensionList 维度列表
     * @return 维度字段处理器
     */
    public static DimensionSetProcessor getDimensionSetProcessor(String key,
                                                                 String metricName,
                                                                 List<Dimension> dimensionList) {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(dimensionList);
        dimensionSetProcessor.setKey(key);
        dimensionSetProcessor.setMetricName(metricName);
        dimensionSetProcessor.init();
        return dimensionSetProcessor;
    }

    /**
     * 生成度量值字段处理器
     *
     * @param fieldMap      宽表字段
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
     * 多表达式字段处理器
     *
     * @param fieldMap
     * @param metricExpressList
     * @return
     */
    @SneakyThrows
    public static MetricListFieldProcessor getMetricListFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       List<String> metricExpressList) {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        metricListFieldProcessor.setFieldMap(fieldMap);
        metricListFieldProcessor.setMetricExpressList(metricExpressList);
        metricListFieldProcessor.init();
        return metricListFieldProcessor;
    }

    /**
     * 生成多字段去重字段处理器
     *
     * @param fieldMap          宽表字段
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
     * @param fieldMap            宽表字段
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

    /**
     * 混合类型字段处理器
     *
     * @param fieldMap
     * @param mixUdafParam
     * @param factory
     * @return
     * @param <IN>
     */
    @SneakyThrows
    public static <IN> MixFieldProcessor<IN> getMixFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  MixUdafParam mixUdafParam,
                                                                  AggregateFunctionFactory factory) {
        MixFieldProcessor<IN> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        mixFieldProcessor.setMixUdafParam(mixUdafParam);
        mixFieldProcessor.setAggregateFunctionFactory(factory);
        mixFieldProcessor.init();
        return mixFieldProcessor;
    }

    /**
     * 映射型字段处理器
     *
     * @param fieldMap
     * @param factory
     * @param mapUdafParam
     * @return
     * @param <IN>
     * @throws Exception
     */
    @SneakyThrows
    public static <IN> MapFieldProcessor<IN> getMapFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  AggregateFunctionFactory factory,
                                                                  MapUdafParam mapUdafParam) {
        MapFieldProcessor<IN> mapFieldProcessor = new MapFieldProcessor<>();
        mapFieldProcessor.setFieldMap(fieldMap);
        mapFieldProcessor.setMapUdafParam(mapUdafParam);
        mapFieldProcessor.setAggregateFunctionFactory(factory);
        mapFieldProcessor.init();
        return mapFieldProcessor;
    }

    /**
     * 聚合字段处理器
     * <p>包含度量字段和聚合函数</p>
     *
     * @param aggregateFunctionParam
     * @param fieldMap
     * @param factory
     * @return
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     */
    @SneakyThrows
    public static <IN, ACC, OUT> AggregateFieldProcessor<IN, ACC, OUT> getAggregateFieldProcessor(
                                                                        AggregateFunctionParam aggregateFunctionParam,
                                                                        Map<String, Class<?>> fieldMap,
                                                                        AggregateFunctionFactory factory) {
        String aggregateType = aggregateFunctionParam.getCalculateLogic();

        AggregateFunction<IN, ACC, OUT> aggregateFunction = factory.getAggregateFunction(aggregateType);
        Class<? extends AggregateFunction> aggregateFunctionClass = aggregateFunction.getClass();

        //如果是基本聚合类型(数值型、集合型、对象型)
        if (aggregateFunctionClass.isAnnotationPresent(Numerical.class)
                || aggregateFunctionClass.isAnnotationPresent(Objective.class)
                || aggregateFunctionClass.isAnnotationPresent(Collective.class)) {
            BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
            AggregateFunctionFactory.setUdafParam(aggregateFunction, baseUdafParam.getParam());
            FieldProcessor<JSONObject, IN> baseFieldProcessor =
                    getBaseFieldProcessor(baseUdafParam, fieldMap, factory);
            return new AggregateFieldProcessor<>(baseFieldProcessor, aggregateFunction);
        }

        //如果是映射类型
        if (aggregateFunctionClass.isAnnotationPresent(MapType.class)) {
            MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();
            AggregateFunctionFactory.setUdafParam(aggregateFunction, mapUdafParam.getParam());

            BaseUdafParam valueUdafParam = mapUdafParam.getValueAggParam();
            AggregateFunction<Object, Object, Object> valueAggregateFunction
                    = factory.getAggregateFunction(valueUdafParam.getAggregateType());
            AggregateFunctionFactory.setUdafParam(valueAggregateFunction, valueUdafParam.getParam());
            ((AbstractMapAggregateFunction<?, Object, Object, Object, OUT>) aggregateFunction).setValueAggregateFunction(valueAggregateFunction);

            MapFieldProcessor<IN> mapFieldProcessor = getMapFieldProcessor(fieldMap, factory, mapUdafParam);
            return new AggregateFieldProcessor<>(mapFieldProcessor, aggregateFunction);
        }

        //如果是混合类型
        if (aggregateFunctionClass.isAnnotationPresent(Mix.class)) {
            MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
            AggregateFunctionFactory.setUdafParam(aggregateFunction, mixUdafParam.getParam());

            //初始化MixFieldProcessor
            MixFieldProcessor<IN> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam, factory);

            AbstractMixAggregateFunction<OUT> abstractMixAggregateFunction = (AbstractMixAggregateFunction<OUT>) aggregateFunction;

            //设置表达式
            String express = mixUdafParam.getExpress();
            //设置反射调用
            AviatorEvaluator.setFunctionMissing(JavaMethodReflectionFunctionMissing.getInstance());
            Expression expression = AviatorEvaluator.getInstance().compile(express, true);
            abstractMixAggregateFunction.setExpression(expression);

            //设置mixAggregateFunctionMap
            Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
            Map<String, BaseUdafParam> mixAggMap = mixUdafParam.getMixAggMap();
            mixAggMap.forEach((tempParam, tempBaseUdafParam) -> {
                AggregateFunction<Object, Object, Object> tempAggregateFunction =
                        factory.getAggregateFunction(tempBaseUdafParam.getAggregateType());
                AggregateFunctionFactory.setUdafParam(tempAggregateFunction, tempBaseUdafParam.getParam());

                mixAggregateFunctionMap.put(tempParam, tempAggregateFunction);
            });
            abstractMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);

            return new AggregateFieldProcessor<>(mixFieldProcessor, aggregateFunction);
        }

        throw new RuntimeException("暂不支持聚合类型: " + aggregateFunctionClass.getName());
    }

    /**
     * 生成基础聚合字段处理器（数值型、对象型和集合型）
     *
     * @param baseUdafParam
     * @param fieldMap
     * @return
     */
    @SneakyThrows
    public static <IN, ACC, OUT> FieldProcessor<JSONObject, IN> getBaseFieldProcessor(
                                                                    BaseUdafParam baseUdafParam,
                                                                    Map<String, Class<?>> fieldMap,
                                                                    AggregateFunctionFactory factory) {

        String aggregateType = baseUdafParam.getAggregateType();
        FieldProcessor<JSONObject, IN> fieldProcessor;
        Class<? extends AggregateFunction> aggregateFunctionClass = factory.getAggregateFunctionClass(aggregateType);
        if (aggregateFunctionClass.isAnnotationPresent(Numerical.class)) {
            //数值型
            NumberFieldProcessor<IN> numberFieldProcessor = new NumberFieldProcessor<>();
            numberFieldProcessor.setUdafParam(baseUdafParam);
            numberFieldProcessor.setFieldMap(fieldMap);
            numberFieldProcessor.setNumerical(aggregateFunctionClass.getAnnotation(Numerical.class));
            fieldProcessor = numberFieldProcessor;
        } else if (aggregateFunctionClass.isAnnotationPresent(Objective.class)) {
            //对象型
            Objective objective = aggregateFunctionClass.getAnnotation(Objective.class);
            ObjectFieldProcessor<IN> objectFieldProcessor = new ObjectFieldProcessor<>();
            objectFieldProcessor.setObjective(objective);
            objectFieldProcessor.setUdafParam(baseUdafParam);
            objectFieldProcessor.setFieldMap(fieldMap);
            fieldProcessor = objectFieldProcessor;
        } else if (aggregateFunctionClass.isAnnotationPresent(Collective.class)) {
            //集合型
            CollectionFieldProcessor<IN> collectionFieldProcessor = new CollectionFieldProcessor<>();
            collectionFieldProcessor.setUdafParam(baseUdafParam);
            collectionFieldProcessor.setFieldMap(fieldMap);
            collectionFieldProcessor.setCollective(aggregateFunctionClass.getAnnotation(Collective.class));
            fieldProcessor = collectionFieldProcessor;
        } else {
            throw new RuntimeException("不支持的聚合类型: " + aggregateType);
        }
        fieldProcessor.init();
        return fieldProcessor;
    }

}
