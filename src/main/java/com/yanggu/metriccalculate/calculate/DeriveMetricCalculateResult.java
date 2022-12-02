package com.yanggu.metriccalculate.calculate;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * 派生指标计算类
 */
@Data
public class DeriveMetricCalculateResult {

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
     * 维度和页面定义的维度顺序一致
     */
    private LinkedHashMap<String, Object> dimensionMap;

    /**
     * 派生指标计算结果指标值
     */
    private Object result;

}
