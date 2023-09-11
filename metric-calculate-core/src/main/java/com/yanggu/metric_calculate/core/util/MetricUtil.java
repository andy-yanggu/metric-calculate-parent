package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.field.FieldCalculate;
import com.yanggu.metric_calculate.core.calculate.field.RealFieldCalculate;
import com.yanggu.metric_calculate.core.calculate.field.VirtualFieldCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.enums.FieldTypeEnum;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn;
import com.yanggu.metric_calculate.core.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import com.yanggu.metric_calculate.core.window.WindowFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.enums.FieldTypeEnum.REAL;
import static com.yanggu.metric_calculate.core.enums.FieldTypeEnum.VIRTUAL;
import static com.yanggu.metric_calculate.core.enums.MetricTypeEnum.DERIVE;

/**
 * 指标工具类
 * <p>主要是初始化指标</p>
 */
@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    /**
     * 初始化指标计算类
     *
     * @param tableData
     * @return
     */
    @SneakyThrows
    public static MetricCalculate initMetricCalculate(Model tableData) {
        if (tableData == null) {
            throw new RuntimeException("明细宽表为空");
        }

        MetricCalculate metricCalculate = BeanUtil.copyProperties(tableData, MetricCalculate.class);

        Map<String, MetricTypeEnum> metricTypeMap = new HashMap<>();
        metricCalculate.setMetricTypeMap(metricTypeMap);

        //设置宽表字段
        setFieldMap(metricCalculate);

        //初始化AviatorFunctionFactory
        List<String> aviatorFunctionJarPathList = metricCalculate.getAviatorFunctionJarPathList();
        AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory(aviatorFunctionJarPathList);
        aviatorFunctionFactory.init();

        //初始化字段计算
        initFieldCalculate(metricCalculate, aviatorFunctionFactory);

        //初始化派生指标
        initAllDerive(metricCalculate, aviatorFunctionFactory);

        return metricCalculate;
    }

    /**
     * 初始化字段计算
     *
     * @param metricCalculate
     * @param aviatorFunctionFactory
     */
    private static void initFieldCalculate(MetricCalculate metricCalculate,
                                           AviatorFunctionFactory aviatorFunctionFactory) {
        if (metricCalculate == null) {
            return;
        }
        if (aviatorFunctionFactory == null) {
            return;
        }
        List<ModelColumn> modelColumnList = metricCalculate.getModelColumnList();
        if (CollUtil.isEmpty(modelColumnList)) {
            return;
        }
        List<FieldCalculate<JSONObject, Object>> fieldCalculateList = new ArrayList<>();
        for (ModelColumn modelColumn : modelColumnList) {
            FieldTypeEnum fieldType = modelColumn.getFieldType();
            //真实字段
            if (REAL.equals(fieldType)) {
                RealFieldCalculate<Object> realFieldCalculate = new RealFieldCalculate<>();
                realFieldCalculate.setColumnName(modelColumn.getName());
                realFieldCalculate.setDataClass((Class<Object>) modelColumn.getDataType().getType());
                realFieldCalculate.init();
                fieldCalculateList.add(realFieldCalculate);
                //虚拟字段
            } else if (VIRTUAL.equals(fieldType)) {
                VirtualFieldCalculate<Object> virtualFieldCalculate = new VirtualFieldCalculate<>();
                virtualFieldCalculate.setColumnName(modelColumn.getName());
                virtualFieldCalculate.setAviatorExpressParam(modelColumn.getAviatorExpressParam());
                virtualFieldCalculate.setFieldMap(metricCalculate.getFieldMap());
                virtualFieldCalculate.setAviatorFunctionFactory(aviatorFunctionFactory);
                virtualFieldCalculate.init();
                fieldCalculateList.add(virtualFieldCalculate);
            } else {
                throw new RuntimeException("字段类型异常");
            }
        }
        metricCalculate.setFieldCalculateList(fieldCalculateList);
    }

    /**
     * 初始化所有派生指标
     *
     * @param metricCalculate
     * @param aviatorFunctionFactory
     */
    @SneakyThrows
    private static void initAllDerive(MetricCalculate metricCalculate,
                                      AviatorFunctionFactory aviatorFunctionFactory) {
        if (metricCalculate == null) {
            return;
        }
        List<DeriveMetrics> deriveMetricsList = metricCalculate.getDeriveMetricsList();
        if (CollUtil.isEmpty(deriveMetricsList)) {
            return;
        }
        Map<String, MetricTypeEnum> metricTypeMap = metricCalculate.getMetricTypeMap();
        //默认是内存的并发HashMap
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleStore.init();

        //初始化聚合函数工厂类
        List<String> udafJarPathList = metricCalculate.getUdafJarPathList();
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(udafJarPathList);
        aggregateFunctionFactory.init();

        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        List<DeriveMetricCalculate> collect = deriveMetricsList.stream()
                .map(tempDerive -> {
                    metricTypeMap.put(tempDerive.getName(), DERIVE);
                    //初始化派生指标计算类
                    DeriveMetricCalculate deriveMetricCalculate =
                            MetricUtil.initDeriveMetrics(tempDerive, fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
                    deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
                    return deriveMetricCalculate;
                })
                .toList();

        metricCalculate.setDeriveMetricCalculateList(collect);
    }

    /**
     * 初始化派生指标
     *
     * @param deriveMetrics
     * @param aviatorFunctionFactory
     * @param aggregateFunctionFactory
     * @return
     */
    @SneakyThrows
    public static <IN, ACC, OUT> DeriveMetricCalculate<IN, ACC, OUT> initDeriveMetrics(
                                                                   DeriveMetrics deriveMetrics,
                                                                   Map<String, Class<?>> fieldMap,
                                                                   AviatorFunctionFactory aviatorFunctionFactory,
                                                                   AggregateFunctionFactory aggregateFunctionFactory) {

        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate = new DeriveMetricCalculate<>();

        //设置id
        Long id = deriveMetrics.getId();
        deriveMetricCalculate.setId(id);

        //设置key
        String key = deriveMetrics.getModelId() + "_" + id;
        deriveMetricCalculate.setKey(key);

        //设置name
        String name = deriveMetrics.getName();
        deriveMetricCalculate.setName(name);

        //设置前置过滤条件处理器
        FilterFieldProcessor filterFieldProcessor =
                FieldProcessorUtil.getFilterFieldProcessor(fieldMap, deriveMetrics.getFilterExpressParam(), aviatorFunctionFactory);
        deriveMetricCalculate.setFilterFieldProcessor(filterFieldProcessor);

        //设置聚合字段处理器
        AggregateFunctionParam aggregateFunctionParam = deriveMetrics.getAggregateFunctionParam();
        AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor =
                FieldProcessorUtil.getAggregateFieldProcessor(fieldMap, aggregateFunctionParam, aviatorFunctionFactory, aggregateFunctionFactory);

        //时间字段处理器
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(deriveMetrics.getTimeColumn());

        //设置WindowFactory
        WindowFactory<IN, ACC, OUT> windowFactory = new WindowFactory<>();
        windowFactory.setWindowParam(deriveMetrics.getWindowParam());
        windowFactory.setTimeFieldProcessor(timeFieldProcessor);
        windowFactory.setAggregateFieldProcessor(aggregateFieldProcessor);
        windowFactory.setFieldMap(fieldMap);
        windowFactory.setAviatorFunctionFactory(aviatorFunctionFactory);
        deriveMetricCalculate.setWindowFactory(windowFactory);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, deriveMetrics.getDimensionList());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(deriveMetrics.getRoundAccuracy());

        //设置是否包含当前笔
        deriveMetricCalculate.setIncludeCurrent(deriveMetrics.getIncludeCurrent());

        return deriveMetricCalculate;
    }

    public static void setFieldMap(MetricCalculate metricCalculate) {
        if (metricCalculate == null) {
            throw new RuntimeException("传入的明细宽表为空");
        }
        List<ModelColumn> fields = metricCalculate.getModelColumnList();
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate));
        }
        //宽表字段
        Map<String, Class<?>> fieldMap = getFieldMap(fields);
        metricCalculate.setFieldMap(fieldMap);
    }

    public static Map<String, Class<?>> getFieldMap(List<ModelColumn> fields) {
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空");
        }
        //宽表字段
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fields.forEach(temp -> fieldMap.put(temp.getName(), temp.getDataType().getType()));
        return fieldMap;
    }

}