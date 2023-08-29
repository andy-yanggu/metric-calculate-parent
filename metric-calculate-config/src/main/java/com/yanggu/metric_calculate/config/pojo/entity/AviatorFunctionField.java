package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Aviator函数字段模板 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aviator_function_field")
public class AviatorFunctionField extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5711687238733531339L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 字段名
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
     * Aviator函数id
     */
    private Integer aviatorFunctionId;

    /**
     * 索引
     */
    private Integer sort;

}
