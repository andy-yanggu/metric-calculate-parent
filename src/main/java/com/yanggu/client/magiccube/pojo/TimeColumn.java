package com.yanggu.client.magiccube.pojo;

import lombok.Data;

/**
 * 时间字段
 */
@Data
public class TimeColumn {

    /**
     * 时间字段名
     */
    private String columnName;

    /**
     * 时间格式
     */
    private String timeFormat;

    private String columnIndex;

}
