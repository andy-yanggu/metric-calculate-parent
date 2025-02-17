package com.yanggu.metric_calculate.core.field_process;


import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.*;
import com.yanggu.metric_calculate.core.aggregate_function.map.AbstractMapAggregateFunction;
import com.yanggu.metric_calculate.core.aggregate_function.mix.AbstractMixAggregateFunction;
import com.yanggu.metric_calculate.core.field_process.aggregate.*;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.metric_list.MetricListFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.multi_field.MultiFieldDataFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelTimeColumn;
import com.yanggu.metric_calculate.core.pojo.udaf_param.*;
import com.yanggu.metric_calculate.core.util.AviatorExpressUtil;
import lombok.SneakyThrows;

import java.util.*;

/**
 * 字段处理器工具类
 */
public class FieldProcessorUtil {

    private FieldProcessorUtil() {
    }

    /**
     * 生成前置过滤条件字段字段处理器
     *
     * @param fieldMap           宽表字段
     * @param filterExpressParam 过滤表达式
     * @return 前置过滤条件字段处理器
     */
    @SneakyThrows
    public static FilterFieldProcessor getFilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                                               AviatorExpressParam filterExpressParam,
                                                               AviatorFunctionFactory aviatorFunctionFactory) {
        FilterFieldProcessor filterFieldProcessor = new FilterFieldProcessor(fieldMap, filterExpressParam, aviatorFunctionFactory);
        filterFieldProcessor.init();
        return filterFieldProcessor;
    }

    /**
     * 生成时间字段处理器
     *
     * @param modelTimeColumn 时间字段(字段字段名和时间格式)
     * @return 时间字段处理器
     */
    public static TimeFieldProcessor getTimeFieldProcessor(ModelTimeColumn modelTimeColumn) {
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(modelTimeColumn.getTimeFormat(), modelTimeColumn.getColumnName());
        timeFieldProcessor.init();
        return timeFieldProcessor;
    }

    /**
     * 生成维度字段处理器
     *
     * @param key           指标唯一标识
     * @param metricName    指标名称
     * @param modelDimensionColumnList 维度列表
     * @return 维度字段处理器
     */
    public static DimensionSetProcessor getDimensionSetProcessor(String key,
                                                                 String metricName,
                                                                 List<ModelDimensionColumn> modelDimensionColumnList) {
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(modelDimensionColumnList);
        dimensionSetProcessor.setKey(key);
        dimensionSetProcessor.setMetricName(metricName);
        dimensionSetProcessor.init();
        return dimensionSetProcessor;
    }

    /**
     * 生成度量值字段处理器
     *
     * @param aviatorExpressParam 度量表达式
     * @param fieldMap            宽表字段
     * @return 度量值字段处理器
     */
    @SneakyThrows
    public static <R> MetricFieldProcessor<R> getMetricFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                      AviatorExpressParam aviatorExpressParam,
                                                                      AviatorFunctionFactory aviatorFunctionFactory) {
        MetricFieldProcessor<R> metricFieldProcessor = new MetricFieldProcessor<>(fieldMap, aviatorExpressParam, aviatorFunctionFactory);
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
        MetricListFieldProcessor metricListFieldProcessor = new MetricListFieldProcessor(fieldMap, metricExpressParamList, aviatorFunctionFactory);
        metricListFieldProcessor.init();
        return metricListFieldProcessor;
    }

    /**
     * 生成多字段数据字段处理器
     *
     * @param fieldMap                宽表字段
     * @param aviatorExpressParamList 表达式列表
     * @return 多字段去重字段处理器
     */
    @SneakyThrows
    public static MultiFieldDataFieldProcessor getMultiFieldDataFieldProcessor(
                                                                    Map<String, Class<?>> fieldMap,
                                                                    List<AviatorExpressParam> aviatorExpressParamList,
                                                                    AviatorFunctionFactory aviatorFunctionFactory) {
        MultiFieldDataFieldProcessor tempMultiFieldDataFieldProcessor =
                new MultiFieldDataFieldProcessor(fieldMap, aviatorExpressParamList, aviatorFunctionFactory);
        tempMultiFieldDataFieldProcessor.init();
        return tempMultiFieldDataFieldProcessor;
    }

    /**
     * 数值型字段提取器
     *
     * @param fieldMap
     * @param baseUdafParam
     * @param numerical
     * @param aviatorFunctionFactory
     * @param <IN>
     * @return
     */
    @SneakyThrows
    private static <IN> NumberFieldProcessor<IN> getNumberFieldProcessor(
                                                                     Map<String, Class<?>> fieldMap,
                                                                     BaseUdafParam baseUdafParam,
                                                                     Numerical numerical,
                                                                     AviatorFunctionFactory aviatorFunctionFactory) {
        NumberFieldProcessor<IN> numberFieldProcessor = new NumberFieldProcessor<>(fieldMap, baseUdafParam, numerical, aviatorFunctionFactory);
        numberFieldProcessor.init();
        return numberFieldProcessor;
    }

    /**
     * 集合型字段处理器
     *
     * @param fieldMap
     * @param baseUdafParam
     * @param collective
     * @param aviatorFunctionFactory
     * @param <IN>
     * @return
     */
    @SneakyThrows
    private static <IN> CollectionFieldProcessor<IN> getCollectionFieldProcessor(
                                                                        Map<String, Class<?>> fieldMap,
                                                                        BaseUdafParam baseUdafParam,
                                                                        Collective collective,
                                                                        AviatorFunctionFactory aviatorFunctionFactory) {
        CollectionFieldProcessor<IN> collectionFieldProcessor = new CollectionFieldProcessor<>(fieldMap, baseUdafParam, collective, aviatorFunctionFactory);
        collectionFieldProcessor.init();
        return collectionFieldProcessor;
    }

    /**
     * 对象型字段处理器
     *
     * @param fieldMap
     * @param baseUdafParam
     * @param objective
     * @param aviatorFunctionFactory
     * @param <IN>
     * @return
     */
    @SneakyThrows
    private static <IN> ObjectFieldProcessor<IN> getObjectFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                         BaseUdafParam baseUdafParam,
                                                                         Objective objective,
                                                                         AviatorFunctionFactory aviatorFunctionFactory) {
        ObjectFieldProcessor<IN> objectFieldProcessor = new ObjectFieldProcessor<>(fieldMap, baseUdafParam, objective, aviatorFunctionFactory);
        objectFieldProcessor.init();
        return objectFieldProcessor;
    }

    /**
     * 映射型字段处理器
     *
     * @param fieldMap
     * @param mapUdafParam
     * @param aggregateFunctionFactory
     * @param <IN>
     * @return
     */
    @SneakyThrows
    public static <IN> MapFieldProcessor<IN> getMapFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  MapUdafParam mapUdafParam,
                                                                  AviatorFunctionFactory aviatorFunctionFactory,
                                                                  AggregateFunctionFactory aggregateFunctionFactory) {
        MapFieldProcessor<IN> mapFieldProcessor = new MapFieldProcessor<>(fieldMap, mapUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
        mapFieldProcessor.init();
        return mapFieldProcessor;
    }

    /**
     * 混合类型字段处理器
     *
     * @param fieldMap
     * @param mixUdafParam
     * @param aggregateFunctionFactory
     * @param <IN>
     * @return
     */
    @SneakyThrows
    public static <IN> MixFieldProcessor<IN> getMixFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                  MixUdafParam mixUdafParam,
                                                                  AviatorFunctionFactory aviatorFunctionFactory,
                                                                  AggregateFunctionFactory aggregateFunctionFactory) {
        MixFieldProcessor<IN> mixFieldProcessor = new MixFieldProcessor<>(fieldMap, mixUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
        mixFieldProcessor.init();
        return mixFieldProcessor;
    }

    /**
     * 生成基础聚合字段处理器（数值型、对象型和集合型）
     *
     * @param fieldMap
     * @param baseUdafParam
     * @return
     */
    @SneakyThrows
    public static <IN> FieldProcessor<Map<String, Object>, IN> getBaseAggregateFieldProcessor(
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
     * @param fieldMap
     * @param aggregateFunctionParam
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
            FieldProcessor<Map<String, Object>, IN> baseFieldProcessor =
                    getBaseAggregateFieldProcessor(fieldMap, baseUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, baseUdafParam.getParam());
            return new AggregateFieldProcessor<>(baseFieldProcessor, aggregateFunction);
        }

        //如果是映射类型
        if (aggregateFunctionClass.isAnnotationPresent(MapType.class)) {
            MapUdafParam mapUdafParam = aggregateFunctionParam.getMapUdafParam();

            MapFieldProcessor<IN> mapFieldProcessor = getMapFieldProcessor(fieldMap, mapUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, mapUdafParam.getParam());

            BaseUdafParam valueUdafParam = mapUdafParam.getValueAggParam();
            AggregateFunction<Object, Object, Object> valueAggregateFunction
                    = aggregateFunctionFactory.getAggregateFunction(valueUdafParam.getAggregateType());
            AggregateFunctionFactory.initAggregateFunction(valueAggregateFunction, valueUdafParam.getParam());
            ((AbstractMapAggregateFunction) aggregateFunction).setValueAggregateFunction(valueAggregateFunction);
            return new AggregateFieldProcessor<>(mapFieldProcessor, aggregateFunction);
        }

        //如果是混合类型
        if (aggregateFunctionClass.isAnnotationPresent(Mix.class)) {
            MixUdafParam mixUdafParam = aggregateFunctionParam.getMixUdafParam();
            AggregateFunctionFactory.initAggregateFunction(aggregateFunction, mixUdafParam.getParam());

            //初始化MixFieldProcessor
            MixFieldProcessor<IN> mixFieldProcessor = getMixFieldProcessor(fieldMap, mixUdafParam, aviatorFunctionFactory, aggregateFunctionFactory);

            AbstractMixAggregateFunction<OUT> abstractMixAggregateFunction = (AbstractMixAggregateFunction<OUT>) aggregateFunction;

            List<MixUdafParamItem> mixUdafParamItemList = mixUdafParam.getMixUdafParamItemList();

            //设置mixAggregateFunctionMap
            Set<String> keySet = new HashSet<>();
            Map<String, AggregateFunction> mixAggregateFunctionMap = new HashMap<>();
            mixUdafParamItemList.forEach(mixUdafParamItem -> {
                String name = mixUdafParamItem.getName();
                keySet.add(name);
                BaseUdafParam tempBaseUdafParam = mixUdafParamItem.getBaseUdafParam();
                AggregateFunction tempAggregateFunction = null;
                if (tempBaseUdafParam != null) {
                    tempAggregateFunction = aggregateFunctionFactory.getAggregateFunction(tempBaseUdafParam.getAggregateType());
                    AggregateFunctionFactory.initAggregateFunction(tempAggregateFunction, tempBaseUdafParam.getParam());
                }
                MapUdafParam mapUdafParam = mixUdafParamItem.getMapUdafParam();
                if (mapUdafParam != null) {
                    tempAggregateFunction = aggregateFunctionFactory.getAggregateFunction(mapUdafParam.getAggregateType());
                    AggregateFunctionFactory.initAggregateFunction(tempAggregateFunction, mapUdafParam.getParam());
                    BaseUdafParam valueUdafParam = mapUdafParam.getValueAggParam();
                    AggregateFunction<Object, Object, Object> valueAggregateFunction
                            = aggregateFunctionFactory.getAggregateFunction(valueUdafParam.getAggregateType());
                    AggregateFunctionFactory.initAggregateFunction(valueAggregateFunction, valueUdafParam.getParam());
                    ((AbstractMapAggregateFunction) tempAggregateFunction).setValueAggregateFunction(valueAggregateFunction);
                }
                if (tempAggregateFunction == null) {
                    throw new RuntimeException("MixUdafParamItem中聚合函数参数错误");
                }
                mixAggregateFunctionMap.put(name, tempAggregateFunction);
            });
            abstractMixAggregateFunction.setMixAggregateFunctionMap(mixAggregateFunctionMap);

            //设置表达式
            Expression expression = AviatorExpressUtil.compileExpress(mixUdafParam.getMetricExpressParam(), aviatorFunctionFactory);
            AviatorExpressUtil.checkVariable(expression, keySet);
            abstractMixAggregateFunction.setExpression(expression);

            return new AggregateFieldProcessor<>(mixFieldProcessor, aggregateFunction);
        }

        throw new RuntimeException("暂不支持聚合类型: " + aggregateFunctionClass.getName());
    }

}
