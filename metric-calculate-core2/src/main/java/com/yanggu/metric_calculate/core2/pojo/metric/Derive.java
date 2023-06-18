package com.yanggu.metric_calculate.core2.pojo.metric;

import lombok.Data;

import java.util.List;


/**
 * 派生指标
 */
@Data
public class Derive {

    /**
     * 派生指标id
     */
    private Long id;

    /**
     * 派生指标名称
     */
    private String name;

    /**
     * 派生指标中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 维度字段
     */
    private List<Dimension> dimension;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

    /**
     * 前置过滤条件
     */
    private String filter;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParam aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
    private WindowParam windowParam;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

}
