package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MapType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.fieldprocess.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.*;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.MapUnitUdafParam;
import com.yanggu.metric_calculate.core.pojo.NumberObjectCollectionUdafParam;
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
     * @param baseUdafParam
     * @param unitFactory
     * @param fieldMap
     * @param aggregateType
     * @return
     * @throws Exception
     */
    public static BaseAggregateFieldProcessor<?> getBaseAggregateFieldProcessor(NumberObjectCollectionUdafParam baseUdafParam,
                                                                                UnitFactory unitFactory,
                                                                                Map<String, Class<?>> fieldMap,
                                                                                String aggregateType) throws Exception {

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor;
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
                                                                                      String aggregateType,
                                                                                      Map<String, Class<?>> fieldMap,
                                                                                      UnitFactory unitFactory) throws Exception {
        AggregateMapUnitFieldProcessor<?> aggregateMapUnitFieldProcessor = new AggregateMapUnitFieldProcessor<>();
        aggregateMapUnitFieldProcessor.setMapUnitUdafParam(mapUnitUdafParam);
        aggregateMapUnitFieldProcessor.setUnitFactory(unitFactory);
        aggregateMapUnitFieldProcessor.setAggregateType(aggregateType);
        aggregateMapUnitFieldProcessor.setMergeUnitClazz(unitFactory.getMergeableClass(aggregateType));
        aggregateMapUnitFieldProcessor.setFieldMap(fieldMap);
        aggregateMapUnitFieldProcessor.init();

        return aggregateMapUnitFieldProcessor;
    }

    public static AggregateFieldProcessor<?> getAggregateFieldProcessor(NumberObjectCollectionUdafParam baseUdafParam,
                                                                        MapUnitUdafParam mapUdafParam,
                                                                        String aggregateType,
                                                                        Map<String, Class<?>> fieldMap,
                                                                        UnitFactory unitFactory) throws Exception {
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(aggregateType);

        //如果是基本聚合类型(数值型、集合型、对象型)
        if (mergeUnitClazz.isAnnotationPresent(Numerical.class) || mergeUnitClazz.isAnnotationPresent(Objective.class)
                || mergeUnitClazz.isAnnotationPresent(Collective.class)) {
            return getBaseAggregateFieldProcessor(baseUdafParam, unitFactory, fieldMap, aggregateType);
        }

        //如果是映射类型
        if (mergeUnitClazz.isAnnotationPresent(MapType.class)) {
            return getAggregateMapUnitFieldProcessor(mapUdafParam, aggregateType, fieldMap, unitFactory);
        }

        throw new RuntimeException("暂不支持聚合类型: " + mergeUnitClazz.getName());
    }

}
