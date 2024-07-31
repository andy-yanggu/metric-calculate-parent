package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数 实体类。
 */
@Data
@Table(value = "aviator_function")
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -990784661474142084L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 唯一标识
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
     * 是否内置: 0否, 1是
     */
    private Boolean isBuiltIn;

    /**
     * jar存储id
     */
    private Integer jarStoreId;

    /**
     * 不是内置的聚合函数为外置jar
     */
    @RelationManyToOne(selfField = "jarStoreId", targetField = "id")
    private JarStoreEntity jarStore;

    /**
     * Aviator函数成员变量列表
     */
    @RelationOneToMany(selfField = "id", targetField = "aviatorFunctionId")
    private List<AviatorFunctionFieldEntity> aviatorFunctionFieldList;

}
