package com.yanggu.metric_calculate.core.util;

import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.field.FieldCalculate;
import com.yanggu.metric_calculate.core.calculate.field.RealFieldCalculate;
import com.yanggu.metric_calculate.core.calculate.field.VirtualFieldCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.GlobalMetricCalculate;
import com.yanggu.metric_calculate.core.enums.FieldTypeEnum;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.field_process.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn;
import com.yanggu.metric_calculate.core.pojo.metric.AggregateFunctionParam;
import com.yanggu.metric_calculate.core.pojo.metric.Derive;
import com.yanggu.metric_calculate.core.pojo.metric.Global;
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
import java.util.stream.Collectors;

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

        //初始化全局指标
        initAllGlobal(metricCalculate, aviatorFunctionFactory);

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
        List<ModelColumn> modelColumnList = metricCalculate.getFieldList();
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
                virtualFieldCalculate.setAviatorExpressParam(modelColumn.getExpressParam());
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
        List<Derive> deriveList = metricCalculate.getDeriveList();
        if (CollUtil.isEmpty(deriveList)) {
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

        Long tableId = metricCalculate.getId();
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        List<DeriveMetricCalculate> collect = deriveList.stream()
                .map(tempDerive -> {
                    metricTypeMap.put(tempDerive.getName(), DERIVE);
                    //初始化派生指标计算类
                    DeriveMetricCalculate deriveMetricCalculate =
                            MetricUtil.initDerive(tempDerive, tableId, fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
                    deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
                    return deriveMetricCalculate;
                })
                .collect(Collectors.toList());

        metricCalculate.setDeriveMetricCalculateList(collect);
    }

    @SneakyThrows
    private static void initAllGlobal(MetricCalculate metricCalculate, AviatorFunctionFactory aviatorFunctionFactory) {
        if (metricCalculate == null) {
            return;
        }
        List<Global> globalList = metricCalculate.getGlobalList();
        if (CollUtil.isEmpty(globalList)) {
            return;
        }

        Map<String, MetricTypeEnum> metricTypeMap = metricCalculate.getMetricTypeMap();
        //默认是内存的并发HashMap
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleStore.init();

        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(metricCalculate.getUdafJarPathList());
        aggregateFunctionFactory.init();

        Long tableId = metricCalculate.getId();
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        List<GlobalMetricCalculate> collect = globalList.stream()
                .map(tempGlobal -> {
                    metricTypeMap.put(tempGlobal.getName(), DERIVE);
                    //初始化派生指标计算类
                    GlobalMetricCalculate globalMetricCalculate =
                            MetricUtil.initGlobal(tempGlobal, tableId, fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
                    globalMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);
                    return globalMetricCalculate;
                })
                .collect(Collectors.toList());

        metricCalculate.setGlobalMetricCalculateList(collect);
    }

    /**
     * 初始化派生指标
     *
     * @param tempDerive
     * @param aviatorFunctionFactory
     * @param aggregateFunctionFactory
     * @return
     */
    @SneakyThrows
    public static <IN, ACC, OUT> DeriveMetricCalculate<IN, ACC, OUT> initDerive(
                                                                 Derive tempDerive,
                                                                 Long tableId,
                                                                 Map<String, Class<?>> fieldMap,
                                                                 AviatorFunctionFactory aviatorFunctionFactory,
                                                                 AggregateFunctionFactory aggregateFunctionFactory) {
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate = new DeriveMetricCalculate<>();

        //设置id
        Long id = tempDerive.getId();
        deriveMetricCalculate.setId(id);

        //设置key
        String key = tableId + "_" + id;
        deriveMetricCalculate.setKey(key);

        //设置name
        String name = tempDerive.getName();
        deriveMetricCalculate.setName(name);

        //设置前置过滤条件处理器
        FilterFieldProcessor filterFieldProcessor =
                FieldProcessorUtil.getFilterFieldProcessor(fieldMap, tempDerive.getFilterExpressParam(), aviatorFunctionFactory);
        deriveMetricCalculate.setFilterFieldProcessor(filterFieldProcessor);

        //设置聚合字段处理器
        AggregateFunctionParam aggregateFunctionParam = tempDerive.getAggregateFunctionParam();
        AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor =
                FieldProcessorUtil.getAggregateFieldProcessor(fieldMap, aggregateFunctionParam, aviatorFunctionFactory, aggregateFunctionFactory);

        //时间字段处理器
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(tempDerive.getTimeColumn());

        //设置WindowFactory
        WindowFactory<IN, ACC, OUT> windowFactory = new WindowFactory<>();
        windowFactory.setWindowParam(tempDerive.getWindowParam());
        windowFactory.setTimeFieldProcessor(timeFieldProcessor);
        windowFactory.setAggregateFieldProcessor(aggregateFieldProcessor);
        windowFactory.setFieldMap(fieldMap);
        windowFactory.setAviatorFunctionFactory(aviatorFunctionFactory);
        deriveMetricCalculate.setWindowFactory(windowFactory);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, tempDerive.getDimensionList());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        //设置是否包含当前笔
        deriveMetricCalculate.setIncludeCurrent(tempDerive.getIncludeCurrent());

        return deriveMetricCalculate;
    }

    @SneakyThrows
    public static <IN, ACC, OUT> GlobalMetricCalculate<IN, ACC, OUT> initGlobal(
                                                                    Global global,
                                                                    Long tableId,
                                                                    Map<String, Class<?>> fieldMap,
                                                                    AviatorFunctionFactory aviatorFunctionFactory,
                                                                    AggregateFunctionFactory aggregateFunctionFactory) {
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate = initDerive(global, tableId, fieldMap, aviatorFunctionFactory, aggregateFunctionFactory);
        return BeanUtil.copyProperties(deriveMetricCalculate, GlobalMetricCalculate.class);
    }

    public static void setFieldMap(MetricCalculate metricCalculate) {
        if (metricCalculate == null) {
            throw new RuntimeException("传入的明细宽表为空");
        }
        List<ModelColumn> fields = metricCalculate.getFieldList();
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate));
        }
        //宽表字段
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fields.forEach(temp -> fieldMap.put(temp.getName(), temp.getDataType().getType()));
        metricCalculate.setFieldMap(fieldMap);
    }

}