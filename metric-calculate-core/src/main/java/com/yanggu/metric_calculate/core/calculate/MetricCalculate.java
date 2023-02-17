package com.yanggu.metric_calculate.core.calculate;


import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.pojo.DataDetailsWideTable;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 指标计算类
 * <p>包含了原子指标、衍生指标、复合指标、全局指标</p>
 */
@Data
public class MetricCalculate<T> extends DataDetailsWideTable {

    /**
     * 原子指标计算类
     */
    private List<AtomMetricCalculate<T, ?>> atomMetricCalculateList;

    /**
     * 派生指标计算类
     */
    private List<DeriveMetricCalculate<T, ?>> deriveMetricCalculateList;

    /**
     * 复合指标计算类
     */
    private List<CompositeMetricCalculate<T>> compositeMetricCalculateList;

    /**
     * 指标名称和指标类型映射
     */
    private Map<String, MetricTypeEnum> metricTypeMap;

    /**
     * 明细宽表字段名称和数据类型的map
     * <p>key 字段名称
     * <p>value class数据类型
     */
    private Map<String, Class<?>> fieldMap;

}

