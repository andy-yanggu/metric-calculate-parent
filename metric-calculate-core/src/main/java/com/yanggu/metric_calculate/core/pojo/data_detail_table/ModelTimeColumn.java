package com.yanggu.metric_calculate.core.pojo.data_detail_table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 宽表时间字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelTimeColumn implements Serializable {

    @Serial
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
