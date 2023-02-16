package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.annotation.*;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.*;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.pojo.MixUnitUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;

import java.util.List;
import java.util.Map;


public class FieldProcessorUtil {

    private FieldProcessorUtil() {
    }

    /**
     * 生成度量值字段处理器
     *
     * @param fieldMap
     * @param metricExpress
     * @return
     * @throws Exception
     */
    public static MetricFieldProcessor<Object> getMetricFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       String metricExpress) throws Exception {
        MetricFieldProcessor<Object> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setFieldMap(fieldMap);
        metricFieldProcessor.setMetricExpress(metricExpress);
        metricFieldProcessor.init();
        return metricFieldProcessor;
    }

    /**
     * 生成多字段去重字段处理器
     *
     * @param fieldMap
     * @param metricExpressList
     * @return
     * @throws Exception
     */
    public static MultiFieldDistinctFieldProcessor getDistinctFieldFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                                  List<String> metricExpressList) throws Exception {
        MultiFieldDistinctFieldProcessor tempMultiFieldDistinctFieldProcessor = new MultiFieldDistinctFieldProcessor();
        tempMultiFieldDistinctFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldDistinctFieldProcessor.setMetricExpressList(metricExpressList);
        tempMultiFieldDistinctFieldProcessor.init();
        return tempMultiFieldDistinctFieldProcessor;
    }

    /**
     * 生成多字段排序字段处理器
     *
     * @param fieldMap
     * @param fieldOrderParamList
     * @return
     * @throws Exception
     */
    public static MultiFieldOrderFieldProcessor getOrderFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                       List<FieldOrderParam> fieldOrderParamList) throws Exception {
        MultiFieldOrderFieldProcessor tempMultiFieldOrderFieldProcessor = new MultiFieldOrderFieldProcessor();
        tempMultiFieldOrderFieldProcessor.setFieldMap(fieldMap);
        tempMultiFieldOrderFieldProcessor.setFieldOrderParamList(fieldOrderParamList);
        tempMultiFieldOrderFieldProcessor.init();
        return tempMultiFieldOrderFieldProcessor;
    }

    /**
     * @param baseUdafParamList
     * @param unitFactory
     * @param fieldMap
     * @return
     * @throws Exception
     */
    public static BaseAggregateFieldProcessor<?> getBaseAggregateFieldProcessor(List<BaseUdafParam> baseUdafParamList,
                                                                                UnitFactory unitFactory,
                                                                                Map<String, Class<?>> fieldMap) throws Exception {

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor;
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
                ((AggregateCollectionFieldProcessor<?>) aggregateFieldProcessor)
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

    public static AggregateMapUnitFieldProcessor<?> getAggregateMapUnitFieldProcessor(MapUnitUdafParam mapUnitUdafParam,
                                                                                      Map<String, Class<?>> fieldMap,
                                                                                      UnitFactory unitFactory) throws Exception {
        AggregateMapUnitFieldProcessor<?> aggregateMapUnitFieldProcessor = new AggregateMapUnitFieldProcessor<>();
        aggregateMapUnitFieldProcessor.setMapUnitUdafParam(mapUnitUdafParam);
        aggregateMapUnitFieldProcessor.setUnitFactory(unitFactory);
        String aggregateType = mapUnitUdafParam.getAggregateType();
        aggregateMapUnitFieldProcessor.setAggregateType(aggregateType);
        aggregateMapUnitFieldProcessor.setMergeUnitClazz(unitFactory.getMergeableClass(aggregateType));
        aggregateMapUnitFieldProcessor.setFieldMap(fieldMap);
        aggregateMapUnitFieldProcessor.init();

        return aggregateMapUnitFieldProcessor;
    }

    public static AggregateFieldProcessor<?> getAggregateMixUnitFieldProcessor(MixUnitUdafParam mixUnitUdafParam,
                                                                               Map<String, Class<?>> fieldMap,
                                                                               UnitFactory unitFactory) throws Exception {

        AggregateMixUnitFieldProcessor<?> mixUnitFieldProcessor = new AggregateMixUnitFieldProcessor<>();
        mixUnitFieldProcessor.setMixUnitUdafParam(mixUnitUdafParam);
        mixUnitFieldProcessor.setFieldMap(fieldMap);
        mixUnitFieldProcessor.setUnitFactory(unitFactory);
        mixUnitFieldProcessor.init();
        return mixUnitFieldProcessor;
    }

    public static AggregateFieldProcessor<?> getAggregateFieldProcessor(List<BaseUdafParam> baseUdafParamList,
                                                                        MapUnitUdafParam mapUdafParam,
                                                                        MixUnitUdafParam mixUnitUdafParam,
                                                                        String aggregateType,
                                                                        Map<String, Class<?>> fieldMap,
                                                                        UnitFactory unitFactory) throws Exception {
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
