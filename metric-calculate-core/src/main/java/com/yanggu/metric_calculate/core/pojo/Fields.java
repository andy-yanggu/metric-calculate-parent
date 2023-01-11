package com.yanggu.metric_calculate.core.pojo;


import com.yanggu.metric_calculate.core.enums.BasicType;
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

    private String labels;

    /**
     * 字段名
     */
    private String name;

    private String properties;

    /**
     * 字段数据类型
     */
    private BasicType valueType;

}
