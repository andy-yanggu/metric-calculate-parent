package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.metric.MetricFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.Store;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 原子指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class AtomMetricCalculate<E> implements Calculate<JSONObject, E> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterFieldProcessor filterFieldProcessor;

    /**
     * 度量字段处理器, 提取出度量值
     */
    private MetricFieldProcessor<E> metricFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 维度字段处理器
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 存储宽表
     */
    private Store store;

    @SneakyThrows
    @Override
    public E exec(JSONObject jsonObject) {
        //执行前置过滤条件
        if (Boolean.FALSE.equals(filterFieldProcessor.process(jsonObject))) {
            return null;
        }

        //执行度量表达式, 提取出度量字段的值
        return metricFieldProcessor.process(jsonObject);
    }

}
