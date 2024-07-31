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
 * 派生指标维度字段中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "derive_model_dimension_column_relation")
public class DeriveModelDimensionColumnRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6517027739456307877L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 维度字段id
     */
    private Integer modelDimensionColumnId;

    /**
     * 序号
     */
    private Integer sort;

}
