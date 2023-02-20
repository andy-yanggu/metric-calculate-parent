package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.annotation.*;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.*;
import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.Dimension;
import com.yanggu.metric_calculate.core.pojo.TimeColumn;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import lombok.SneakyThrows;

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
    public static <T> FilterFieldProcessor<T> getFilterFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                      String filterExpress) {
        FilterFieldProcessor<T> filterFieldProcessor = new FilterFieldProcessor<>(fieldMap, filterExpress);
        filterFieldProcessor.init();
        return filterFieldProcessor;
    }

    /**
     * 生成时间字段处理器
     *
     * @param timeColumn 时间字段(字段字段名和时间格式)
     * @return 时间字段处理器
     */
    public static <T> TimeFieldProcessor<T> getTimeFieldProcessor(TimeColumn timeColumn) {
        TimeFieldProcessor<T> timeFieldProcessor =
                new TimeFieldProcessor<>(timeColumn.getTimeFormat(), timeColumn.getColumnName());
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
    public static <T> DimensionSetProcessor<T> getDimensionSetProcessor(String key, String metricName,
                                                                        Map<String, Class<?>> fieldMap,
                                                                        List<Dimension> dimensionList) {
        DimensionSetProcessor<T> dimensionSetProcessor = new DimensionSetProcessor<>(dimensionList);
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
    public static <T> MetricFieldProcessor<T, Object> getMetricFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                              String metricExpress) {
        MetricFieldProcessor<T, Object> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setFieldMap(fieldMap);
        metricFieldProcessor.setMetricExpress(metricExpress);
        metricFieldProcessor.init();
        return metricFieldProcessor;
    }

    /**
     * 生成多字段去重字段处理器
     *
     * @param fieldMap 宽表字段
     * @param metricExpressList 维度表达式列表
     * @return 多字段去重字段处理器
     */
    @SneakyThrows
    public static <T> MultiFieldDistinctFieldProcessor<T> getDistinctFieldFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                                         List<String> metricExpressList) {
        MultiFieldDistinctFieldProcessor<T> tempMultiFieldDistinctFieldProcessor = new MultiFieldDistinctFieldProcessor<>();
        tempMultiFieldDistinctFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldDistinctFieldProcessor.setMetricExpressList(metricExpressList);
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
    public static <T> MultiFieldOrderFieldProcessor<T> getOrderFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                              List<FieldOrderParam> fieldOrderParamList) {
        MultiFieldOrderFieldProcessor<T> tempMultiFieldOrderFieldProcessor = new MultiFieldOrderFieldProcessor<>();
        tempMultiFieldOrderFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldOrderFieldProcessor.setFieldOrderParamList(fieldOrderParamList);
        tempMultiFieldOrderFieldProcessor.init();
        return tempMultiFieldOrderFieldProcessor;
    }

    /**
     * 生成基础聚合字段处理器（数值型、对象型和集合型）
     *
     * @param baseUdafParamList
     * @param unitFactory
     * @param fieldMap
     * @return
     */
    @SneakyThrows
    public static <T> BaseAggregateFieldProcessor<T, ?> getBaseAggregateFieldProcessor(List<BaseUdafParam> baseUdafParamList,
                                                                                       UnitFactory unitFactory,
                                                                                       Map<String, Class<?>> fieldMap) {

        BaseAggregateFieldProcessor<T, ?> aggregateFieldProcessor;
        BaseUdafParam baseUdafParam = baseUdafParamList.get(0);
        String aggregateType = baseUdafParam.getAggregateType();
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(aggregateType);
        if (mergeUnitClazz.isAnnotationPresent(Numerical.class)) {
            //数值型
            aggregateFieldProcessor = new AggregateNumberFieldProcessor<>();
        } else if (mergeUnitClazz.isAnnotationPresent(Objective.class)) {
            //对象型
            aggregateFieldProcessor = new AggregateObjectFieldProcessor<>();
        } else if (mergeUnitClazz.isAnnotationPresent(Collective.class)) {
            //集合型
            aggregateFieldProcessor = new AggregateCollectionFieldProcessor<>();
            if (mergeUnitClazz.getAnnotation(MergeType.class).useExternalAgg()) {
                ((AggregateCollectionFieldProcessor<T, ?>) aggregateFieldProcessor)
                        .setExternalBaseUdafParam(baseUdafParamList.get(1));
            }
        } else {
            throw new RuntimeException("不支持的聚合类型: " + aggregateType);
        }

        //聚合字段处理器
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(aggregateType);
        aggregateFieldProcessor.setUdafParam(baseUdafParam);
        aggregateFieldProcessor.setUnitFactory(unitFactory);
        aggregateFieldProcessor.setMergeUnitClazz(mergeUnitClazz);
        aggregateFieldProcessor.init();
        return aggregateFieldProcessor;
    }

    /**
     * 生成映射类型聚合字段处理器
     *
     * @param mapUnitUdafParam
     * @param fieldMap
     * @param unitFactory
     * @return
     */
    @SneakyThrows
    public static <T> AggregateMapUnitFieldProcessor<T, ?> getAggregateMapUnitFieldProcessor(MapUnitUdafParam mapUnitUdafParam,
                                                                                             Map<String, Class<?>> fieldMap,
                                                                                             UnitFactory unitFactory) {
        AggregateMapUnitFieldProcessor<T, ?> aggregateMapUnitFieldProcessor = new AggregateMapUnitFieldProcessor<>();
        aggregateMapUnitFieldProcessor.setMapUnitUdafParam(mapUnitUdafParam);
        aggregateMapUnitFieldProcessor.setUnitFactory(unitFactory);
        String aggregateType = mapUnitUdafParam.getAggregateType();
        aggregateMapUnitFieldProcessor.setAggregateType(aggregateType);
        aggregateMapUnitFieldProcessor.setMergeUnitClazz(unitFactory.getMergeableClass(aggregateType));
        aggregateMapUnitFieldProcessor.setFieldMap(fieldMap);
        aggregateMapUnitFieldProcessor.init();

        return aggregateMapUnitFieldProcessor;
    }

    /**
     * 生成混合型聚合字段处理器
     *
     * @param mixUnitUdafParam
     * @param fieldMap
     * @param unitFactory
     * @return
     */
    @SneakyThrows
    public static <T> AggregateFieldProcessor<T, ?> getAggregateMixUnitFieldProcessor(MixUnitUdafParam mixUnitUdafParam,
                                                                                      Map<String, Class<?>> fieldMap,
                                                                                      UnitFactory unitFactory) {

        AggregateMixUnitFieldProcessor<T, ?> mixUnitFieldProcessor = new AggregateMixUnitFieldProcessor<>();
        mixUnitFieldProcessor.setMixUnitUdafParam(mixUnitUdafParam);
        mixUnitFieldProcessor.setFieldMap(fieldMap);
        mixUnitFieldProcessor.setUnitFactory(unitFactory);
        mixUnitFieldProcessor.init();
        return mixUnitFieldProcessor;
    }

    /**
     * 生成聚合字段处理器
     *
     * @param baseUdafParamList
     * @param mapUdafParam
     * @param mixUnitUdafParam
     * @param aggregateType
     * @param fieldMap
     * @param unitFactory
     * @return
     */
    public static <T> AggregateFieldProcessor<T, ?> getAggregateFieldProcessor(List<BaseUdafParam> baseUdafParamList,
                                                                               MapUnitUdafParam mapUdafParam,
                                                                               MixUnitUdafParam mixUnitUdafParam,
                                                                               String aggregateType,
                                                                               Map<String, Class<?>> fieldMap,
                                                                               UnitFactory unitFactory) {
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(aggregateType);

        //如果是基本聚合类型(数值型、集合型、对象型)
        if (mergeUnitClazz.isAnnotationPresent(Numerical.class) || mergeUnitClazz.isAnnotationPresent(Objective.class)
                || mergeUnitClazz.isAnnotationPresent(Collective.class)) {
            return getBaseAggregateFieldProcessor(baseUdafParamList, unitFactory, fieldMap);
        }

        //如果是映射类型
        if (mergeUnitClazz.isAnnotationPresent(MapType.class)) {
            return getAggregateMapUnitFieldProcessor(mapUdafParam, fieldMap, unitFactory);
        }

        if (mergeUnitClazz.isAnnotationPresent(Mix.class)) {
            return getAggregateMixUnitFieldProcessor(mixUnitUdafParam, fieldMap, unitFactory);
        }

        throw new RuntimeException("暂不支持聚合类型: " + mergeUnitClazz.getName());
    }

}
