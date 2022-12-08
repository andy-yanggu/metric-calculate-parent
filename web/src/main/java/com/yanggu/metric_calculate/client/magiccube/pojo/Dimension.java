package com.yanggu.metric_calculate.client.magiccube.pojo;

import lombok.Data;

/**
 * 维度信息
 */
@Data
public class Dimension {

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
