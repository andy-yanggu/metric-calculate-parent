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
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;
import com.yanggu.metric_calculate.core.middle_store.AbstractDeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelColumn;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import com.yanggu.metric_calculate.core.pojo.udaf_param.AggregateFunctionParam;
import com.yanggu.metric_calculate.core.window.WindowFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONUtil;

import java.util.*;
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
            throw new RuntimeException("传入的计算类为空");
        }
        if (aviatorFunctionFactory == null) {
            throw new RuntimeException("传入的AviatorFunctionFactory为空");
        }
        List<ModelColumn> modelColumnList = metricCalculate.getModelColumnList();
        if (CollUtil.isEmpty(modelColumnList)) {
            throw new RuntimeException("宽表字段为空");
        }
        List<FieldCalculate<Map<String, Object>, Object>> fieldCalculateList = new ArrayList<>();
        Set<String> fieldNameSet = metricCalculate.getFieldMap().keySet();
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
        //因为字段之间可能存在依赖关系，所以需要构建依赖图，重新排序字段计算顺序
        //构建右向左依赖图
        Map<String, Set<String>> rightGraph = fieldCalculateList.stream()
                .collect(Collectors.toMap(FieldCalculate::getName, FieldCalculate::dependFields));
        //转换成左向右依赖图
        Map<String, Set<String>> leftGraph = DAGUtil.rightToLeft(rightGraph);

        //进行拓扑排序
        List<List<String>> lists = DAGUtil.parallelTopologicalSort(leftGraph);
        //重新进行排序
        List<List<FieldCalculate<Map<String, Object>, Object>>> fieldCalculateListList = new ArrayList<>();
        for (List<String> list : lists) {
            List<FieldCalculate<Map<String, Object>, Object>> tempFieldCalculateList = new ArrayList<>();
            fieldCalculateListList.add(tempFieldCalculateList);
            for (String fieldName : list) {
                tempFieldCalculateList.add(fieldCalculateList.stream()
                        .filter(fieldCalculate -> fieldCalculate.getName().equals(fieldName))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("字段计算类不存在")));
            }
        }
        metricCalculate.setFieldCalculateListList(fieldCalculateListList);
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
            throw new RuntimeException("传入的计算类为空");
        }
        List<DeriveMetrics> deriveMetricsList = metricCalculate.getDeriveMetricsList();
        if (CollUtil.isEmpty(deriveMetricsList)) {
            return;
        }
        Map<String, MetricTypeEnum> metricTypeMap = metricCalculate.getMetricTypeMap();

        //初始化聚合函数工厂类
        List<String> udafJarPathList = metricCalculate.getUdafJarPathList();
        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(udafJarPathList);
        aggregateFunctionFactory.init();

        //初始化kryo工具类
        KryoPool kryoPool = new KryoPool(100, AggregateFunctionFactory.ACC_CLASS_LOADER);
        InputPool inputPool = new InputPool(100);
        OutputPool outputPool = new OutputPool(100);
        KryoUtil kryoUtil = new KryoUtil(kryoPool, inputPool, outputPool);
        metricCalculate.setKryoUtil(kryoUtil);

        //默认是内存的并发HashMap
        AbstractDeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleStore.setKryoUtil(kryoUtil);
        deriveMetricMiddleStore.init();

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

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, deriveMetrics.getDimensionList());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

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