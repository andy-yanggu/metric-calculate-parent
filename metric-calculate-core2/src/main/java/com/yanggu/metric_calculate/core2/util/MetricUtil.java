package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core2.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.Fields;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标工具类
 */
@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    /**
     * 初始化派生指标
     *
     * @param tempDerive
     * @return
     */
    @SneakyThrows
    public static <T, IN, ACC, OUT> DeriveMetricCalculate<T, IN, ACC, OUT> initDerive(Derive tempDerive,
                                                                                      MetricCalculate<T> metricCalculate) {
        DeriveMetricCalculate<T, IN, ACC, OUT> deriveMetricCalculate = new DeriveMetricCalculate<>();

        //设置key
        String key = metricCalculate.getId() + "_" + tempDerive.getId();
        deriveMetricCalculate.setKey(key);

        //设置name
        String name = tempDerive.getName();
        deriveMetricCalculate.setName(name);

        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();

        //设置前置过滤条件处理器
        FilterFieldProcessor<T> filterFieldProcessor =
                FieldProcessorUtil.getFilterFieldProcessor(fieldMap, tempDerive.getFilter());
        deriveMetricCalculate.setFilterFieldProcessor(filterFieldProcessor);

        //设置聚合字段处理器
        //AggregateFieldProcessor<T, M> aggregateFieldProcessor =
        //        FieldProcessorUtil.getAggregateFieldProcessor(tempDerive, fieldMap, unitFactory);
        //
        //deriveMetricCalculate.setAggregateFieldProcessor(aggregateFieldProcessor);

        //时间字段处理器
        TimeFieldProcessor<T> timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(tempDerive.getTimeColumn());
        deriveMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //设置时间聚合粒度
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(tempDerive.getDuration(), tempDerive.getTimeUnit());
        deriveMetricCalculate.setTimeBaselineDimension(timeBaselineDimension);

        //维度字段处理器
        DimensionSetProcessor<T> dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, fieldMap, tempDerive.getDimension());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        ////设置MetricCubeFactory
        //MetricCubeFactory<M> metricCubeFactory = new MetricCubeFactory<>();
        //metricCubeFactory.setKey(key);
        //metricCubeFactory.setName(name);
        //metricCubeFactory.setTimeBaselineDimension(timeBaselineDimension);
        //metricCubeFactory.setMergeUnitClazz(aggregateFieldProcessor.getMergeUnitClazz());
        //metricCubeFactory.setDerive(tempDerive);
        //
        //deriveMetricCalculate.setMetricCubeFactory(metricCubeFactory);

        return deriveMetricCalculate;
    }

    public static <T> Map<String, Class<?>> getFieldMap(MetricCalculate<T> metricCalculate) {
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
    public static Map<String, Object> getParam(JSONObject input, Map<String, Class<?>> fieldMap) {
        if (CollUtil.isEmpty((Map<?, ?>) input)) {
            throw new RuntimeException("输入数据为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        Map<String, Object> params = new HashMap<>();
        fieldMap.forEach((key, tempDataClass) -> {
            Object value = input.get(key);
            if (value == null) {
                return;
            }
            if (!value.getClass().equals(tempDataClass)) {
                value = Convert.convert(tempDataClass, value);
            }
            params.put(key, value);
        });
        return params;
    }

}