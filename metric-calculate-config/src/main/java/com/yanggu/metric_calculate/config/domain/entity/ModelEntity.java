package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
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
public class ModelEntity extends BaseUserEntity implements Serializable {

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
    private List<ModelColumnEntity> modelColumnList;

    /**
     * 时间字段列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId", orderBy = "sort")
    private List<ModelTimeColumnEntity> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId", orderBy = "sort")
    private List<ModelDimensionColumnEntity> modelDimensionColumnList;

    /**
     * 原子指标列表
     */
    @RelationOneToMany(selfField = "id", targetField = "modelId")
    private List<AtomEntity> atomList;

    /**
     * 派生指标列表
     */
    @RelationManyToMany(
            joinTable = "atom",
            selfField = "id", joinSelfColumn = "model_id",
            targetField = "atomId", joinTargetColumn = "id"
    )
    private List<DeriveEntity> deriveList;

}
