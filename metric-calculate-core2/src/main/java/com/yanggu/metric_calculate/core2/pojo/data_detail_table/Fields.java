package com.yanggu.metric_calculate.core2.pojo.data_detail_table;


import com.yanggu.metric_calculate.core2.enums.BasicType;
import lombok.Data;

/**
 * 宽表字段信息
 */
@Data
public class Fields {

    /**
     * 字段类名
     */
    private String className;

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段数据类型
     */
    private BasicType valueType;

}
