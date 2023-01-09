package com.yanggu.metric_calculate.core.calculate;


import com.yanggu.metric_calculate.client.magiccube.pojo.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 指标计算类
 * 包含了原子指标、衍生指标、复合指标
 */
@Data
public class MetricCalculate extends DataDetailsWideTable {

    /**
     * 原子指标计算类
     */
    private List<AtomMetricCalculate> atomMetricCalculateList;

    /**
     * 派生指标计算类
     */
    private List<DeriveMetricCalculate> deriveMetricCalculateList;

    /**
     * 复合指标计算类
     */
    private List<CompositeMetricCalculate> compositeMetricCalculateList;

    /**
     * 派生指标的上下文, 用于缓存本地聚合值
     */
    private TimedKVMetricContext taskContext;

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

