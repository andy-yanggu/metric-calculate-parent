package com.yanggu.metric_calculate.core.pojo.data_detail_table;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表维度字段
 */
@Data
public class ModelDimensionColumn implements Serializable {

    @Serial
    private static final long serialVersionUID = 4960485684122091771L;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 绑定维度名称
     */
    private String dimensionName;

    /**
     * 维度的顺序
     */
    private Integer columnIndex;

}
