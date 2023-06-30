package com.yanggu.metric_calculate.core2.pojo.aviator_express;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式配置类
 */
@Data
public class AviatorExpressParam implements Serializable {

    private static final long serialVersionUID = -3978431196327161916L;

    /**
     * 表达式
     */
    private String express;

    /**
     * 是否使用自定义Aviator函数
     * <p>默认不使用</p>
     */
    private Boolean useUdfFunction = false;

    /**
     * 自定义Aviator函数参数
     */
    private List<UdfAviatorFunctionParam> udfAviatorFunctionParamList;

}
