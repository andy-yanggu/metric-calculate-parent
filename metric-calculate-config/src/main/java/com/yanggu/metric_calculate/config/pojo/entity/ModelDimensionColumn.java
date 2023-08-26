package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 维度字段 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "model_dimension_column")
public class ModelDimensionColumn extends BaseEntity implements Serializable {

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
     * 宽表字段
     */
    @RelationOneToOne(selfField = "modelColumnId", targetField = "id")
    private ModelColumn modelColumn;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度
     */
    @RelationManyToOne(selfField = "dimensionId", targetField = "id")
    private Dimension dimension;

    /**
     * 索引
     */
    private Integer sort;

}
