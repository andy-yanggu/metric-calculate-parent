package com.yanggu.metric_calculate.core.pojo;

import com.yanggu.metric_calculate.core.enums.TimeUnit;
import lombok.Data;

import java.util.List;
import java.util.Map;


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
     * 聚合逻辑
     */
    private String calculateLogic;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

    /**
     * 用户自定义聚合函数的参数
     */
    private Map<String, Object> udafParams;

    /**
     * 度量字段
     */
    private MetricColumn metricColumn;

    /**
     * 对于集合型和对象型聚合类型, 不保留对象, 保留指定字段
     */
    private String retainExpress;

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
