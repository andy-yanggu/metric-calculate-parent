package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionQuery extends PageQuery<AggregateFunctionEntity> {

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
