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
 * 混合聚合参数选项-基本聚合参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "mix_udaf_param_item_base_udaf_param_relation")
public class MixUdafParamItemBaseUdafParamRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1472213678803702950L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 混合聚合参数选项id
     */
    private Integer mixUdafParamItemId;

    /**
     * 基本聚合参数id
     */
    private Integer baseUdafParamId;

}
