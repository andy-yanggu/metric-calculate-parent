package com.yanggu.metric_calculate.config.pojo.req;

import lombok.Data;

@Data
public class AggregateFunctionQueryReq {

    /**
     * 聚合函数名称
     */
    private String aggregateFunctionName;

    /**
     * 聚合函数中文名
     */
    private String aggregateFunctionDisplayName;

    /**
     * 排序字段名
     */
    private String orderByColumnName;

    /**
     * 是否升序
     */
    private Boolean asc;

}
