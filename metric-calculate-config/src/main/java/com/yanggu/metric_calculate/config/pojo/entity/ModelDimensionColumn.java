package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 维度字段 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "model_dimension_column")
public class ModelDimensionColumn implements Serializable {

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

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    @Column(onInsertValue = "0", isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP", onUpdateValue = "CURRENT_TIMESTAMP")
    private Date updateTime;

}
