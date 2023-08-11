package com.yanggu.metric_calculate.core2.pojo.metric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 时间字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeColumn implements Serializable {

    private static final long serialVersionUID = -1398712073824800181L;

    /**
     * 时间字段名
     */
    private String columnName;

    /**
     * 时间格式
     */
    private String timeFormat;

}
