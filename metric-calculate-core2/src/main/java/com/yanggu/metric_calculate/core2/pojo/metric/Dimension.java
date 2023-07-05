package com.yanggu.metric_calculate.core2.pojo.metric;

import lombok.Data;

import java.io.Serializable;

/**
 * 维度信息
 */
@Data
public class Dimension implements Serializable {

    private static final long serialVersionUID = 4960485684122091771L;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 维度的顺序
     */
    private Integer columnIndex;

    /**
     * 绑定维度名称
     */
    private String dimensionName;

}
