package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数 实体类。
 */
@Data
@Table(value = "aviator_function")
@EqualsAndHashCode(callSuper = true)
public class AviatorFunction extends BaseEntity implements Serializable {

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
     * Aviator函数成员变量列表
     */
    @RelationOneToMany(selfField = "id", targetField = "aviatorFunctionId")
    private List<AviatorFunctionField> aviatorFunctionFieldList;

}
