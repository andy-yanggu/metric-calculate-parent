package com.yanggu.metric_calculate.core.pojo.data_detail_table;


import com.yanggu.metric_calculate.core.enums.BasicType;
import lombok.Data;

/**
 * 宽表字段信息
 */
@Data
public class Fields {

    /**
     * 字段名
     */
    private String name;

    /**
     * 派生指标中文名
     */
    private String displayName;

    /**
     * 字段数据类型
     */
    private BasicType valueType;

    /**
     * 描述信息
     */
    private String description;

}
