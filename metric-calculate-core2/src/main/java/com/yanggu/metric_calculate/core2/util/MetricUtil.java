package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.aggregate_function.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AbstractAggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.Fields;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.core2.enums.MetricTypeEnum.DERIVE;

/**
 * 指标工具类
 */
@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    public static MetricCalculate initMetricCalculate(DataDetailsWideTable tableData) {
        if (tableData == null) {
            throw new RuntimeException("明细宽表为空");
        }

        MetricCalculate metricCalculate = BeanUtil.copyProperties(tableData, MetricCalculate.class);

        Map<String, MetricTypeEnum> metricTypeMap = new HashMap<>();
        metricCalculate.setMetricTypeMap(metricTypeMap);

        //宽表字段
        Map<String, Class<?>> fieldMap = getFieldMap(metricCalculate);
        metricCalculate.setFieldMap(fieldMap);

        //派生指标
        List<Derive> deriveList = tableData.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            List<DeriveMetricCalculate> collect = deriveList.stream()
                    .map(tempDerive -> {
                        metricTypeMap.put(tempDerive.getName(), DERIVE);
                        //初始化派生指标计算类
                        return MetricUtil.initDerive(tempDerive, metricCalculate);
                    })
                    .collect(Collectors.toList());

            //List<Class<? extends MergedUnit>> classList = new ArrayList<>();
            //for (DeriveMetricCalculate<T, ?> deriveMetricCalculate : collect) {
            //    if (Boolean.TRUE.equals(deriveMetricCalculate.getIsUdaf())) {
            //        Class<? extends MergedUnit> mergeUnitClazz = deriveMetricCalculate.getAggregateFieldProcessor().getMergeUnitClazz();
            //        classList.add(mergeUnitClazz);
            //    }
            //}
            ////派生指标中间结算结果存储接口
            //Map<String, DeriveMetricMiddleStore> metricMiddleStoreMap =
            //        AbstractDeriveMetricMiddleStore.DeriveMetricMiddleStoreHolder.getStoreMap();
            ////默认是内存的并发HashMap
            //DeriveMetricMiddleStore deriveMetricMiddleStore = metricMiddleStoreMap.get(DEFAULT_IMPL);
            ////初始化KryoPool
            //KryoPool kryoPool = new KryoPool(true, true, 100);
            //InputPool inputPool = new InputPool(true, true, 100);
            //OutputPool outputPool = new OutputPool(true, true, 100);
            //KryoUtil.init(kryoPool, inputPool, outputPool);
            //if (metricMiddleStoreMap.size() != 1) {
            //    for (Map.Entry<String, DeriveMetricMiddleStore> middleStoreEntry : metricMiddleStoreMap.entrySet()) {
            //        if (!StrUtil.equals(DEFAULT_IMPL, middleStoreEntry.getKey())) {
            //            deriveMetricMiddleStore = middleStoreEntry.getValue();
            //            break;
            //        }
            //    }
            //}
            //DeriveMetricMiddleStore finalDeriveMetricMiddleStore = deriveMetricMiddleStore;
            //collect.forEach(temp -> temp.setDeriveMetricMiddleStore(finalDeriveMetricMiddleStore));
            metricCalculate.setDeriveMetricCalculateList(collect);
        }

        //复合指标
        //List<Composite> compositeList = tableData.getComposite();
        //if (CollUtil.isNotEmpty(compositeList)) {
        //    List<CompositeMetricCalculate<T>> collect = new ArrayList<>();
        //    compositeList.forEach(compositeMetric -> {
        //        metricTypeMap.put(compositeMetric.getName(), COMPOSITE);
        //
        //        //初始化复合指标计算类
        //        List<CompositeMetricCalculate<T>> compositeMetricCalculateList =
        //                MetricUtil.initComposite(compositeMetric, metricCalculate);
        //        collect.addAll(compositeMetricCalculateList);
        //    });
        //    metricCalculate.setCompositeMetricCalculateList(collect);
        //}
        //
        ////全局指标
        //List<Global> globalList = tableData.getGlobal();
        //if (CollUtil.isNotEmpty(globalList)) {
        //    globalList.forEach(temp -> metricTypeMap.put(temp.getName(), GLOBAL));
        //}
        return metricCalculate;
    }

    /**
     * 初始化派生指标
     *
     * @param tempDerive
     * @return
     */
    @SneakyThrows
    public static <IN, ACC, OUT> DeriveMetricCalculate<IN, ACC, OUT> initDerive(Derive tempDerive,
                                                                                MetricCalculate metricCalculate) {
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate = new DeriveMetricCalculate<>();

        //设置key
        String key = metricCalculate.getId() + "_" + tempDerive.getId();
        deriveMetricCalculate.setKey(key);

        //设置name
        String name = tempDerive.getName();
        deriveMetricCalculate.setName(name);

        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();

        //设置前置过滤条件处理器
        FilterFieldProcessor filterFieldProcessor =
                FieldProcessorUtil.getFilterFieldProcessor(fieldMap, tempDerive.getFilter());
        deriveMetricCalculate.setFilterFieldProcessor(filterFieldProcessor);

        AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory(tempDerive.getUdafJarPathList());
        aggregateFunctionFactory.init();

        //设置聚合字段处理器
        AbstractAggregateFieldProcessor<IN, ACC, OUT> abstractAggregateFieldProcessor =
                FieldProcessorUtil.getAbstractAggregateFieldProcessor(tempDerive, fieldMap, aggregateFunctionFactory);
        deriveMetricCalculate.setAggregateFieldProcessor(abstractAggregateFieldProcessor);

        //时间字段处理器
        TimeFieldProcessor timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(tempDerive.getTimeColumn());
        deriveMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //设置时间聚合粒度
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(tempDerive.getDuration(), tempDerive.getTimeUnit());
        deriveMetricCalculate.setTimeBaselineDimension(timeBaselineDimension);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, fieldMap, tempDerive.getDimension());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        return deriveMetricCalculate;
    }

    public static Map<String, Class<?>> getFieldMap(MetricCalculate metricCalculate) {
        if (metricCalculate == null) {
            throw new RuntimeException("传入的明细宽表为空");
        }
        List<Fields> fields = metricCalculate.getFields();
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate));
        }
        //宽表字段
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fields.forEach(temp -> fieldMap.put(temp.getName(), temp.getValueType().getType()));
        metricCalculate.setFieldMap(fieldMap);
        return fieldMap;
    }

    /**
     * 从原始数据中提取数据, 进行手动数据类型转换
     * <p>防止输入的数据类型和数据明细宽表定义的数据类型不匹配
     * <p>主要是数值型
     *
     * @param input    输入的数据
     * @param fieldMap 宽表字段名称和数据类型
     * @return
     */
    public static JSONObject getParam(JSONObject input, Map<String, Class<?>> fieldMap) {
        if (CollUtil.isEmpty((Map<?, ?>) input)) {
            throw new RuntimeException("输入数据为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        JSONObject returnData = new JSONObject();
        fieldMap.forEach((key, tempDataClass) -> {
            Object value = input.get(key);
            if (value == null) {
                return;
            }
            if (!value.getClass().equals(tempDataClass)) {
                value = Convert.convert(tempDataClass, value);
            }
            returnData.set(key, value);
        });
        return returnData;
    }

}