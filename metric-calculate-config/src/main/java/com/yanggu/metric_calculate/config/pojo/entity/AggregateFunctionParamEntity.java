package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aggregate_function_param")
public class AggregateFunctionParamEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -8675494863416206181L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationManyToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunctionEntity aggregateFunction;

    /**
     * 基本类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_base_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private BaseUdafParamEntity baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_map_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "map_udaf_param_id"
    )
    private MapUdafParamEntity mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_mix_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "mix_udaf_param_id"
    )
    private MixUdafParamEntity mixUdafParam;

}
