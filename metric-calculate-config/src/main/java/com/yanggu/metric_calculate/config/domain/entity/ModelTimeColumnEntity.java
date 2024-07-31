package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 时间字段 实体类。
 */
@Data
@Table(value = "model_time_column")
@EqualsAndHashCode(callSuper = true)
public class ModelTimeColumnEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 518994644896526018L;

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
     * 时间格式
     */
    private String timeFormat;

    /**
     * 索引
     */
    private Integer sort;

}
