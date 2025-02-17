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
 * CEP匹配配置数据表达式关系表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "node_pattern_aviator_express_param_relation")
public class NodePatternAviatorExpressParamRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8086581324531220516L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * CEP匹配配置数据id
     */
    private Integer nodePatternId;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

}
