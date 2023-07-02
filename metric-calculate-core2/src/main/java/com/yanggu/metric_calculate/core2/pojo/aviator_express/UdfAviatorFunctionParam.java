package com.yanggu.metric_calculate.core2.pojo.aviator_express;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义Aviator函数配置类
 */
@Data
public class UdfAviatorFunctionParam implements Serializable {

    private static final long serialVersionUID = 337965557491005190L;

    /**
     * 自定义Aviator函数名称
     */
    private String name;

    /**
     * 是否是自定义udf
     */
    private Boolean isUdf;

    /**
     * Aviator函数的参数, key和Java字段名称一致
     */
    private Map<String, Object> param;

}
