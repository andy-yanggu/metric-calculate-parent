package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 时间字段 实体类。
 */
@Data
@Table(value = "model_time_column")
@EqualsAndHashCode(callSuper = true)
public class ModelTimeColumn extends BaseEntity implements Serializable {

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
     * 宽表字段名
     */
    @Column(ignore = true)
    private String modelColumnName;

    /**
     * 宽表字段
     */
    @RelationOneToOne(selfField = "modelColumnId", targetField = "id")
    private ModelColumn modelColumn;

    /**
     * 时间格式
     */
    private String timeFormat;

    /**
     * 索引
     */
    private Integer sort;

}
