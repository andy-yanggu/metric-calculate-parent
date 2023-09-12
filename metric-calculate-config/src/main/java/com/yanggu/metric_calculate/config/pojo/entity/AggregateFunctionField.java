package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数的字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aggregate_function_field")
public class AggregateFunctionField extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1255374352282442715L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
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
