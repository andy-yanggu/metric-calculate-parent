package com.yanggu.metric_calculate.config.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
public class AggregateFunctionParamDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6999604291880819075L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 聚合函数id
     */
    @NotNull(message = "聚合函数id不能为空")
    private Integer aggregateFunctionId;

    /**
     * 聚合函数
     */
    private AggregateFunctionDTO aggregateFunction;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParamDTO baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParamDTO mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParamDTO mixUdafParam;

}
