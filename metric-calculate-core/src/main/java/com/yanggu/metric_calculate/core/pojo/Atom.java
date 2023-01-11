package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

import java.util.List;

/**
 * 原子指标
 */
@Data
public class Atom {

    /**
     * 原子指标id
     */
    private Long id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 前置过滤条件
     */
    private String filter;

    /**
     * 维度字段
     */
    private List<Dimension> dimension;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

    /**
     * 度量字段
     */
    private MetricColumn metricColumn;

    /**
     * 指标存储相关信息
     */
    private Store store;

}
