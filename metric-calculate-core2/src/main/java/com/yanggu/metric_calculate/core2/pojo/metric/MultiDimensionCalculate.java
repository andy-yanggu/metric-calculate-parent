package com.yanggu.metric_calculate.core2.pojo.metric;

import lombok.Data;

import java.util.List;

/**
 * 多维度计算
 */
@Data
public class MultiDimensionCalculate {

    /**
     * 维度字段
     */
    private List<Dimension> dimension;

    /**
     * 计算表达式
     */
    private String calculateExpression;

}
