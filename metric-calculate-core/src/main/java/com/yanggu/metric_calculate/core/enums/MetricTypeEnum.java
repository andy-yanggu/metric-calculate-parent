package com.yanggu.metric_calculate.core.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标类型枚举
 */
@Getter
@AllArgsConstructor
public enum MetricTypeEnum {

    /**
     * 原子指标
     */
    ATOM("原子指标"),

    /**
     * 派生指标
     */
    DERIVE("派生指标"),

    /**
     * 复合指标
     */
    COMPOSITE("复合指标"),

    ;

    /**
     * 指标类型名称
     */
    private final String metricTypeName;

}
