package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

/**
 * 度量字段
 */
@Data
public class MetricColumn {

    /**
     * 字段名称
     */
    private String columnName;

    private Integer columnIndex;

}
