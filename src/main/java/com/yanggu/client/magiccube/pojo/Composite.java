package com.yanggu.client.magiccube.pojo;

import lombok.Data;

import java.util.List;

/**
 * 复合指标
 */
@Data
public class Composite {

    /**
     * 复合指标的id
     */
    private Long id;

    /**
     * 复合指标中文名
     */
    private String displayName;

    /**
     * 复合指标名称
     */
    private String name;

    /**
     * 维度字段
     */
    private List<Dimension> dimension;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 计算表达式
     */
    private String calculateExpression;

    /**
     * 指标存储相关信息
     */
    private Store store;

}
