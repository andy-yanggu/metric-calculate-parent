package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToOne;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "model_dimension_column")
public class ModelDimensionColumnEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5538736519253522391L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 宽表字段id
     */
    private Integer modelColumnId;

    /**
     * 宽表字段名称
     */
    @Column(ignore = true)
    @RelationOneToOne(selfField = "modelColumnId", targetField = "id", valueField = "name", targetTable = "model_column")
    private String modelColumnName;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度名称
     */
    @Column(ignore = true)
    @RelationManyToOne(selfField = "dimensionId", targetField = "id", valueField = "name", targetTable = "dimension")
    private String dimensionName;

    /**
     * 索引
     */
    private Integer sort;

}
