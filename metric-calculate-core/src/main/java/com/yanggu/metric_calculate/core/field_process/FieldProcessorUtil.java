package com.yanggu.metric_calculate.core.field_process;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.*;
import com.yanggu.metric_calculate.core.aggregate_function.map.AbstractMapAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.mix.AbstractMixAggregateFunction;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.field_process.aggregate.*;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core.pojo.metric.Dimension;
import com.yanggu.metric_calculate.core.pojo.metric.TimeColumn;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import com.yanggu.metric_calculate.core.util.ExpressionUtil;
import lombok.SneakyThrows;
import org.dromara.hutool.json.JSONObject;

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
     * @param filterExpressParam 过滤表达式
     * @return 前置过滤条件字段处理器
     */
    @SneakyThrows
    public static FilterFieldProcessor getFilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                                               AviatorExpressParam filterExpressParam,
                                                               AviatorFunctionFactory aviatorFunctionFactory) {
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor();
        filterFieldProcessor.setFieldMap(fieldMap);
        filterFieldProcessor.setFilterExpressParam(filterExpressParam);
        filterFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
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
     * @param aviatorExpressParam 度量表达式
     * @return 度量值字段处理器
     */
    @SneakyThrows
    public static <R> MetricFieldProcessor<R> getMetricFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                      AviatorExpressParam aviatorExpressParam,
                                                                      AviatorFunctionFactory aviatorFunctionFactory) {
        MetricFieldProcessor<R> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setFieldMap(fieldMap);
        metricFieldProcessor.setAviatorExpressParam(aviatorExpressParam);
        metricFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        metricFieldProcessor.init();
        return metricFieldProcessor;
    }

    /**
     * 多表达式字段处理器
     *
     * @param fieldMap
     * @param metricExpressParamList
     * @return
     */
    @SneakyThrows
    public static MetricListFieldProcessor getMetricListFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       List<AviatorExpressParam> metricExpressParamList,
                                                                       AviatorFunctionFactory aviatorFunctionFactory) {
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor();
        metricListFieldProcessor.setFieldMap(fieldMap);
        metricListFieldProcessor.setMetricExpressParamList(metricExpressParamList);
        metricListFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        metricListFieldProcessor.init();
        return metricListFieldProcessor;
    }

    /**
     * 生成多字段去重字段处理器
     *
     * @param fieldMap          宽表字段
     * @param distinctFieldListParamList 维度表达式列表
     * @return 多字段去重字段处理器
     */
    @SneakyThrows
    public static MultiFieldDistinctFieldProcessor getDistinctFieldFieldProcessor(
                                                              Map<String, Class<?>> fieldMap,
                                                              List<AviatorExpressParam> distinctFieldListParamList,
                                                              AviatorFunctionFactory aviatorFunctionFactory) {
        MultiFieldDistinctFieldProcessor tempMultiFieldDistinctFieldProcessor = new MultiFieldDistinctFieldProcessor();
        tempMultiFieldDistinctFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldDistinctFieldProcessor.setDistinctFieldListParamList(distinctFieldListParamList);
        tempMultiFieldDistinctFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
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
    public static MultiFieldOrderFieldProcessor getFieldOrderFieldProcessor(
                                                                        Map<String, Class<?>> fieldMap,
                                                                        List<FieldOrderParam> fieldOrderParamList,
                                                                        AviatorFunctionFactory aviatorFunctionFactory) {
        MultiFieldOrderFieldProcessor tempMultiFieldOrderFieldProcessor = new MultiFieldOrderFieldProcessor();
        tempMultiFieldOrderFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldOrderFieldProcessor.setFieldOrderParamList(fieldOrderParamList);
        tempMultiFieldOrderFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        tempMultiFieldOrderFieldProcessor.init();
        return tempMultiFieldOrderFieldProcessor;
    }

    /**
     * 数值型字段提取器
     *
     * @param baseUdafParam
     * @param fieldMap
     * @param numerical
     * @param aviatorFunctionFactory
     * @return
     * @param <IN>
     */
    @SneakyThrows
    private static <IN> NumberFieldProcessor<IN> getNumberFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                         BaseUdafParam baseUdafParam,
                                                                         Numerical numerical,
                                                                         AviatorFunctionFactory aviatorFunctionFactory) {
        NumberFieldProcessor<IN> numberFieldProcessor = new NumberFieldProcessor<>();
        numberFieldProcessor.setUdafParam(baseUdafParam);
        numberFieldProcessor.setFieldMap(fieldMap);
        numberFieldProcessor.setNumerical(numerical);
        numberFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        numberFieldProcessor.init();
        return numberFieldProcessor;
    }

    /**
     * 集合型字段处理器
     *
     * @param baseUdafParam
     * @param fieldMap
     * @param collective
     * @param aviatorFunctionFactory
     * @return
     * @param <IN>
     */
    @SneakyThrows
    private static <IN> CollectionFieldProcessor<IN> getCollectionFieldProcessor(
                                                                        Map<String, Class<?>> fieldMap,
                                                                        BaseUdafParam baseUdafParam,
                                                                        Collective collective,
                                                                        AviatorFunctionFactory aviatorFunctionFactory) {
        CollectionFieldProcessor<IN> collectionFieldProcessor = new CollectionFieldProcessor<>();
        collectionFieldProcessor.setUdafParam(baseUdafParam);
        collectionFieldProcessor.setFieldMap(fieldMap);
        collectionFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        collectionFieldProcessor.setCollective(collective);
        collectionFieldProcessor.init();
        return collectionFieldProcessor;
    }

    /**
     * 对象型字段处理器
     *
     * @param baseUdafParam
     * @param fieldMap
     * @param objective
     * @param aviatorFunctionFactory
     * @return
     * @param <IN>
     */
    @SneakyThrows
    private static <IN> ObjectFieldProcessor<IN> getObjectFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                         BaseUdafParam baseUdafParam,
                                                                         Objective objective,
                                                                         AviatorFunctionFactory aviatorFunctionFactory) {
        ObjectFieldProcessor<IN> objectFieldProcessor = new ObjectFieldProcessor<>();
        objectFieldProcessor.setObjective(objective);
        objectFieldProcessor.setUdafParam(baseUdafParam);
        objectFieldProcessor.setFieldMap(fieldMap);
        objectFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        objectFieldProcessor.init();
        return objectFieldProcessor;
    }

    /**
     * 映射型字段处理器
     *
     * @param fieldMap
     * @param aggregateFunctionFactory
     * @param mapUdafParam
     * @return
     * @param <IN>
     * @throws Exception
     */
    @SneakyThrows
    public static <IN> MapFieldProcessor<IN> getMapFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  MapUdafParam mapUdafParam,
                                                                  AviatorFunctionFactory aviatorFunctionFactory,
                                                                  AggregateFunctionFactory aggregateFunctionFactory) {
        MapFieldProcessor<IN> mapFieldProcessor = new MapFieldProcessor<>();
        mapFieldProcessor.setFieldMap(fieldMap);
        mapFieldProcessor.setMapUdafParam(mapUdafParam);
        mapFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        mapFieldProcessor.setAggregateFunctionFactory(aggregateFunctionFactory);
        mapFieldProcessor.init();
        return mapFieldProcessor;
    }

    /**
     * 混合类型字段处理器
     *
     * @param fieldMap
     * @param mixUdafParam
     * @param aggregateFunctionFactory
     * @return
     * @param <IN>
     */
    @SneakyThrows
    public static <IN> MixFieldProcessor<IN> getMixFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  MixUdafParam mixUdafParam,
                                                                  AviatorFunctionFactory aviatorFunctionFactory,
                                                                  AggregateFunctionFactory aggregateFunctionFactory) {
        MixFieldProcessor<IN> mixFieldProcessor = new MixFieldProcessor<>();
        mixFieldProcessor.setFieldMap(fieldMap);
        mixFieldProcessor.setMixUdafParam(mixUdafParam);
        mixFieldProcessor.setAviatorFunctionFactory(aviatorFunctionFactory);
        mixFieldProcessor.setAggregateFunctionFactory(aggregateFunctionFactory);
        mixFieldProcessor.init();
        return mixFieldProcessor;
    }

    /**
     * 生成基础聚合字段处理器（数值型、对象型和集合型）
     *
     * @param baseUdafParam
     * @param fieldMap
     * @return
     */
    @SneakyThrows
    public static <IN> FieldProcessor<JSONObject, IN> getBaseAggregateFieldProcessor(
                                                                Map<String, Class<?>> fieldMap,
                                                                BaseUdafParam baseUdafParam,
                                                                AviatorFunctionFactory aviatorFunctionFactory,
                                                                AggregateFunctionFactory aggregateFunctionFactory) {

        String aggregateType = baseUdafParam.getAggregateType();
        Class<? extends AggregateFunction> aggregateFunctionClass =
                                        aggregateFunctionFactory.getAggregateFunctionClass(aggregateType);
        if (aggregateFunctionClass.isAnnotationPresent(Numerical.class)) {
            //数值型
            Numerical numerical = aggregateFunctionClass.getAnnotation(Numerical.class);
            return getNumberFieldProcessor(fieldMap, baseUdafParam, numerical, aviatorFunctionFactory);
        } else if (aggregateFunctionClass.isAnnotationPresent(Collective.class)) {
            //集合型
            Collective collective = aggregateFunctionClass.getAnnotation(Collective.class);
            return getCollectionFieldProcessor(fieldMap, baseUdafParam, collective, aviatorFunctionFactory);
        } else if (aggregateFunctionClass.isAnnotationPresent(Objective.class)) {
            //对象型
            Objective objective = aggregateFunctionClass.getAnnotation(Objective.class);
            return getObjectFieldProcessor(fieldMap, baseUdafParam, objective, aviatorFunctionFactory);
        } else {
            throw new RuntimeException("不支持的聚合类型: " + aggregateType);
        }
    }

    /**
     * 聚合字段处理器
     * <p>包含度量字段和聚合函数</p>
     *
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     * @param aggregateFunctionParam
     * @param fieldMap
     * @param aviatorFunctionFactory
     * @param aggregateFunctionFactory
     * @return
     */
    @SneakyThrows
    public static <IN, ACC, OUT> AggregateFieldProcessor<IN, ACC, OUT> getAggregateFieldProcessor(
                                                            Map<String, Class<?>> fieldMap,
                                                            AggregateFunctionParam aggregateFunctionParam,
                                                            AviatorFunctionFactory aviatorFunctionFactory,
                                                            AggregateFunctionFactory aggregateFunctionFactory) {
        String aggregateType = aggregateFunctionParam.getAggregateType();

        AggregateFunction<IN, ACC, OUT> aggregateFunction = aggregateFunctionFactory.getAggregateFunction(aggregateType);
        Class<? extends AggregateFunction> aggregateFunctionClass = aggregateFunction.getClass();

        //如果是基本聚合类型(数值型、集合型、对象型)
        if (aggregateFunctionClass.isAnnotationPresent(Numerical.class)
                || aggregateFunctionClass.isAnnotationPresent(Collective.class)
                || aggregateFunctionClass.isAnnotationPresent(Objective.class)) {
            BaseUdafParam baseUdafParam = aggregateFunctionParam.getBaseUdafParam();
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, baseUdafParam.getParam());
            FieldProcessor<JSONObject, IN> baseFieldProcessor =
                    getBaseAggregateFieldProcessor(fieldMap, baseUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            return new AggregateFieldProcessor<>(baseFieldProcessor, aggregateFunction);
        }

        //如果是映射类型
        if (aggregateFunctionClass.isAnnotationPresent(MapType.class)) {
            MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, mapUdafParam.getParam());

            BaseUdafParam valueUdafParam = mapUdafParam.getValueAggParam();
            AggregateFunction<Object, Object, Object> valueAggregateFunction
                    = aggregateFunctionFactory.getAggregateFunction(valueUdafParam.getAggregateType());
            AggregateFunctionFactory.initAggregateFunction(valueAggregateFunction, valueUdafParam.getParam());
            ((AbstractMapAggregateFunction<?, Object, Object, Object, OUT>) aggregateFunction).setValueAggregateFunction(valueAggregateFunction);

            MapFieldProcessor<IN> mapFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            return new AggregateFieldProcessor<>(mapFieldProcessor, aggregateFunction);
        }

        //如果是混合类型
        if (aggregateFunctionClass.isAnnotationPresent(Mix.class)) {
            MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, mixUdafParam.getParam());

            //初始化MixFieldProcessor
            MixFieldProcessor<IN> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);

            AbstractMixAggregateFunction<OUT> abstractMixAggregateFunction = (AbstractMixAggregateFunction<OUT>) aggregateFunction;

            Map<String, BaseUdafParam> mixAggMap = mixUdafParam.getMixAggMap();
            Map<String, Class<?>> tempMap = new HashMap<>();
            for (String key : mixAggMap.keySet()) {
                tempMap.put(key, Object.class);
            }

            //设置表达式
            Expression expression = ExpressionUtil.compileExpress(mixUdafParam.getMetricExpressParam(), aviatorFunctionFactory);
            ExpressionUtil.checkVariable(expression, tempMap);
            abstractMixAggregateFunction.setExpression(expression);

            //设置mixAggregateFunctionMap
            Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
            mixAggMap.forEach((tempParam, tempBaseUdafParam) -> {
                AggregateFunction<Object, Object, Object> tempAggregateFunction =
                        aggregateFunctionFactory.getAggregateFunction(tempBaseUdafParam.getAggregateType());
                AggregateFunctionFactory.initAggregateFunction(tempAggregateFunction, tempBaseUdafParam.getParam());

                mixAggregateFunctionMap.put(tempParam, tempAggregateFunction);
            });
            abstractMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);
            return new AggregateFieldProcessor<>(mixFieldProcessor, aggregateFunction);
        }

        throw new RuntimeException("暂不支持聚合类型: " + aggregateFunctionClass.getName());
    }

}
