package com.yanggu.metric_calculate.config.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionParamDto extends BaseDto implements Serializable {

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
    private AggregateFunctionDto aggregateFunction;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParamDto baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParamDto mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParamDto mixUdafParam;

}
