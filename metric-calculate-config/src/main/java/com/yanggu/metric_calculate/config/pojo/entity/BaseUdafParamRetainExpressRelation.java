package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 基本聚合参数，保留字段表达式中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "base_udaf_param_retain_express_relation")
public class BaseUdafParamRetainExpressRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6972055823638138257L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

    /**
     * Aviator函数参数id
     */
    private Integer aviatorExpressParamId;

}
