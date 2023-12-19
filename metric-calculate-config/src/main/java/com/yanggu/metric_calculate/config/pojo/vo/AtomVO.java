package com.yanggu.metric_calculate.config.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 原子指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AtomVO extends BaseVO implements Serializable {

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
     * 宽表时间字段id
     */
    private Integer modelTimeColumnId;

    /**
     * 时间字段
     */
    private ModelTimeColumnVO modelTimeColumn;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParamVO aggregateFunctionParam;

    /**
     * 目录编码
     */
    private String directoryCode;

}