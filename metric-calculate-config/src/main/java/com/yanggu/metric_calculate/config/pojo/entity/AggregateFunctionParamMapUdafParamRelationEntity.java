package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数-映射聚合参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aggregate_function_param_map_udaf_param_relation")
public class AggregateFunctionParamMapUdafParamRelationEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 708709114968051539L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数参数id
     */
    private Integer aggregateFunctionParamId;

    /**
     * 映射聚合参数id
     */
    private Integer mapUdafParamId;

}
