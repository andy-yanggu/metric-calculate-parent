package com.yanggu.metric_calculate.client.magiccube.pojo;

import com.yanggu.metric_calculate.client.magiccube.enums.TimeUnit;
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
     * 引用的原子指标名
     */
    private String atom;

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
     * 聚合逻辑
     */
    private String calculateLogic;

    /**
     * 度量字段
     */
    private MetricColumn metricColumn;

    /**
     * 聚合时间长度
     */
    private Integer duration;

    /**
     * 聚合时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 存储宽表
     */
    private Store store;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

}
