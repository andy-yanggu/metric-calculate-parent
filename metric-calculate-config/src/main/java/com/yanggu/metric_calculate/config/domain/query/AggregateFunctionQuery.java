package com.yanggu.metric_calculate.config.domain.query;

import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.domain.entity.AggregateFunctionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionQuery extends PageQuery<AggregateFunctionEntity> {

    @Serial
    private static final long serialVersionUID = 2069586775947652687L;

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
