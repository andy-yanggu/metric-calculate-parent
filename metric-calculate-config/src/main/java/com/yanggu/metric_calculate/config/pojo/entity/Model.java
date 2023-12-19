package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 数据明细宽表 实体类。
 */
@Data
@Table(value = "model")
@EqualsAndHashCode(callSuper = true)
public class Model extends BaseEntity implements Serializable {

    @Serial
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

    /**
     * 原子指标列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId")
    private List<Atom> atomList;

    /**
     * 派生指标列表
     */
    @RelationManyToMany(
            joinTable = "atom",
            selfField = "id", joinSelfColumn = "model_id",
            targetField = "atomId", joinTargetColumn = "id"
    )
    private List<Derive> deriveList;

}
