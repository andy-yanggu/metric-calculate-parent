package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数的字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionFieldDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8681127845176848237L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;


    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    /**
     * 索引
     */
    private Integer sort;

}