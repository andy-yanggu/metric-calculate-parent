package com.yanggu.client.magiccube.pojo;

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

    private Integer columnIndex;

    /**
     * 绑定维度名称
     */
    private String dimensionName;

}
