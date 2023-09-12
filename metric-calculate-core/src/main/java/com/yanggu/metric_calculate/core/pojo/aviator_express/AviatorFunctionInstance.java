package com.yanggu.metric_calculate.core.pojo.aviator_express;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Aviator函数实例
 */
@Data
public class AviatorFunctionInstance implements Serializable {

    @Serial
    private static final long serialVersionUID = 337965557491005190L;

    /**
     * 自定义Aviator函数名称
     */
    private String name;

    /**
     * Aviator函数的参数, key和Java字段名称一致
     */
    private Map<String, Object> param;

}
