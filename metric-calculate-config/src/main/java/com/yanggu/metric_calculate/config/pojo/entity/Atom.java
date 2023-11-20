package com.yanggu.metric_calculate.config.pojo.entity;

import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionParamDto;
import com.yanggu.metric_calculate.config.pojo.dto.ModelTimeColumnDto;
import com.yanggu.metric_calculate.config.pojo.dto.WindowParamDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 派生指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Atom extends BaseEntity implements Serializable {

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
    private ModelTimeColumn modelTimeColumn;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParam aggregateFunctionParam;

    /**
     * 目录编码
     */
    private String directoryCode;

}