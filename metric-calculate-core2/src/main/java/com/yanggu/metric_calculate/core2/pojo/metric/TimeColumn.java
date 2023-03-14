package com.yanggu.metric_calculate.core2.pojo.metric;

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

}
