package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 派生指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "atom")
public class AtomEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2005906604838021242L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
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
    @RelationManyToOne(selfField = "modelTimeColumnId", targetField = "id")
    private ModelTimeColumnEntity modelTimeColumn;

    /**
     * 聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "atom_aggregate_function_param_relation",
            selfField = "id", joinSelfColumn = "atom_id",
            targetField = "id", joinTargetColumn = "aggregate_function_param_id")
    private AggregateFunctionParamEntity aggregateFunctionParam;

    /**
     * 目录编码
     */
    private String directoryCode;

}