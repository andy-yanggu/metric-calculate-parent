package com.yanggu.metric_calculate.config.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 原子指标 实体类。
 */
@Data
public class AtomDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2005906604838021242L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 中文名称
     */
    @NotBlank(message = "中文名称不能为空")
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * 宽表id
     */
    @NotNull(message = "宽表id不能为空")
    private Integer modelId;

    /**
     * 宽表时间字段id
     */
    private Integer modelTimeColumnId;

    /**
     * 时间字段
     */
    private ModelTimeColumnDTO modelTimeColumn;

    /**
     * 聚合函数参数
     */
    @Valid
    @NotNull(message = "聚合函数参数不能为空")
    private AggregateFunctionParamDTO aggregateFunctionParam;

    /**
     * 目录编码
     */
    private String directoryCode;

}