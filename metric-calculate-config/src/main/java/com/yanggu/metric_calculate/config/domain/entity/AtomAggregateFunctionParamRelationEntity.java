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
 * 原子指标聚合函数参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "atom_aggregate_function_param_relation")
public class AtomAggregateFunctionParamRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -9031946851432248021L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 原子指标id
     */
    private Integer atomId;

    /**
     * 聚合函数参数id
     */
    private Integer aggregateFunctionParamId;

}
