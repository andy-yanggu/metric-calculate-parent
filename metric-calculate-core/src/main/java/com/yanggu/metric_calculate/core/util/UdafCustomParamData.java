package com.yanggu.metric_calculate.core.util;


import lombok.Data;

@Data
public class UdafCustomParamData {

    /**
     * 参数名
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 能够被修改
     */
    private Boolean update;

    /**
     * 是否必填
     */
    private Boolean notNull;

}
