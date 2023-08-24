package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 混合聚合参数，多个聚合值的计算表达式中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "mix_udaf_param_metric_express_relation")
public class MixUdafParamMetricExpressRelation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3054088426740239233L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 混合聚合函数参数id
     */
    private Integer mixUdafParamId;

    /**
     * Aviator函数参数id
     */
    private Integer aviatorExpressParamId;

}
