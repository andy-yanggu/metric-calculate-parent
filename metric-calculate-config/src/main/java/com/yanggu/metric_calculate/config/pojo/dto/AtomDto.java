package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 原子指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AtomDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2005906604838021242L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 时间字段
     */
    private ModelTimeColumnDto modelTimeColumn;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParamDto aggregateFunctionParam;

    /**
     * 目录编码
     */
    private String directoryCode;

}