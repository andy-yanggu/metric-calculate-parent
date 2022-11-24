package com.yanggu.client.magiccube.pojo;


import com.yanggu.client.magiccube.enums.BasicType;
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
