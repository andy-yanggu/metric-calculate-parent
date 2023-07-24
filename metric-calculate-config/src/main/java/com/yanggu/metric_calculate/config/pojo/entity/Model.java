package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数据明细宽表 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "model")
public class Model implements Serializable {

    private static final long serialVersionUID = 193942509865715855L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 宽表名称
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
     * 目录id
     */
    private Integer directoryId;

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

    /**
     * 宽表字段列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId", orderBy = "sort")
    private List<ModelColumn> modelColumnList;

    /**
     * 时间字段列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId", orderBy = "sort")
    private List<ModelTimeColumn> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId", orderBy = "sort")
    private List<ModelDimensionColumn> modelDimensionColumnList;

}
