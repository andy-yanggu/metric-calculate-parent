package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 派生指标聚合函数参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "derive_aggregate_function_param_relation")
public class DeriveAggregateFunctionParamRelation extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3708893426209998423L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 聚合函数参数id
     */
    private Integer aggregateFunctionParamId;

}
