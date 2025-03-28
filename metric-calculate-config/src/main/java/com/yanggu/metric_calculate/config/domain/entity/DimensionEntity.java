package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 维度表 实体类。
 */
@Data
@Table(value = "dimension")
@EqualsAndHashCode(callSuper = true)
public class DimensionEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6177700319457677745L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 维度名称
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

}
