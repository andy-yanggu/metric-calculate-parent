package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Aviator表达式和混合类型参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aviator_express_param_mix_udaf_param_item_relation")
public class AviatorExpressParamMixUdafParamItemRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1388355865938181382L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

    /**
     * 混合类型参数id
     */
    private Integer mixUdafParamItemId;

}
