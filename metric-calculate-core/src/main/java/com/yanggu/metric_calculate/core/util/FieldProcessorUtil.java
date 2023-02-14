package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.fieldprocess.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateCollectionFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateNumberFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateObjectFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.BaseAggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_distinct.MultiFieldDistinctFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core.fieldprocess.multi_field_order.MultiFieldOrderFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.NumberObjectCollectionUdafParam;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: YangGu
 * @date: 2023/2/14 17:53
 * @description:
 */
public class FieldProcessorUtil {

    private FieldProcessorUtil() {

    }

    /**
     * 新建多字段去重字段处理器
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
     * 新建多字段排序字段处理器
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
     * 新建度量值字段处理器
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
     *
     * @param udafParam
     * @param unitFactory
     * @param fieldMap
     * @param calculateLogic
     * @return
     * @throws Exception
     */
    public static BaseAggregateFieldProcessor<?> getBaseAggregateFieldProcessor(NumberObjectCollectionUdafParam udafParam,
                                                                                UnitFactory unitFactory,
                                                                                Map<String, Class<?>> fieldMap,
                                                                                String calculateLogic) throws Exception {

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor;
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(calculateLogic);
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
            throw new RuntimeException("不支持的聚合类型: " + calculateLogic);
        }

        //聚合字段处理器
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(calculateLogic);
        aggregateFieldProcessor.setIsUdaf(false);
        aggregateFieldProcessor.setUdafParam(udafParam);
        aggregateFieldProcessor.setUnitFactory(unitFactory);
        aggregateFieldProcessor.setMergeUnitClazz(mergeUnitClazz);
        aggregateFieldProcessor.init();
        return aggregateFieldProcessor;
    }

}
