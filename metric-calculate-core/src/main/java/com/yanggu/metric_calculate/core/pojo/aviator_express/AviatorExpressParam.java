package com.yanggu.metric_calculate.core.pojo.aviator_express;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式配置类
 */
@Data
public class AviatorExpressParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -3978431196327161916L;

    /**
     * 表达式
     */
    private String express;

    /**
     * 自定义Aviator函数参数
     */
    private List<AviatorFunctionInstance> aviatorFunctionInstanceList;

}
