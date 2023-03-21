package com.yanggu.metric_calculate.core2.pojo.metric;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 派生指标计算结果类
 */
@Data
public class DeriveMetricCalculateResult<OUT> {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 开始时间
     * yyyy-MM-dd HH:mm:ss格式, 包含开始时间
     */
    private String startTime;

    /**
     * 结束时间
     * yyyy-MM-dd HH:mm:ss格式, 不包含开始时间
     */
    private String endTime;

    /**
     * 维度kv k是维度名称, value是维度值
     * <p>维度和页面定义的维度顺序一致
     */
    private LinkedHashMap<String, Object> dimensionMap;

    /**
     * 派生指标计算结果指标值
     */
    private OUT result;

}
