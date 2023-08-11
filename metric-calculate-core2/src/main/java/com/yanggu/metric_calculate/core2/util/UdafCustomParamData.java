package com.yanggu.metric_calculate.core2.util;


import lombok.Data;

@Data
public class UdafCustomParamData {

    /**
     * 参数名
     */
    private String name;

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

    /**
     * 描述信息
     */
    private String description;

}
