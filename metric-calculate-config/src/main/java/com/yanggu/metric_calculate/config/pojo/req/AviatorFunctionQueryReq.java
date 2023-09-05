package com.yanggu.metric_calculate.config.pojo.req;

import lombok.Data;

@Data
public class AviatorFunctionQueryReq {

    /**
     * Aviator函数名称
     */
    private String aviatorFunctionName;

    /**
     * Aviator函数中文名
     */
    private String aviatorFunctionDisplayName;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
