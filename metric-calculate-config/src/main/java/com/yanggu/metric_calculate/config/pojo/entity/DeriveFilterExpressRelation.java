package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 派生指标前置过滤条件中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "derive_filter_express_relation")
public class DeriveFilterExpressRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 8826727012390952992L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

}
