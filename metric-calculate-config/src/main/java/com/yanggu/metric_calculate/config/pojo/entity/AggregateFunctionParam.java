package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "aggregate_function_param")
public class AggregateFunctionParam implements Serializable {

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
    private AggregateFunction aggregateFunction;

    /**
     * 基本类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_base_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private BaseUdafParam baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_map_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "map_udaf_param_id"
    )
    private MapUdafParam mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "aggregate_function_param_mix_udaf_param_relation",
            selfField = "id", joinSelfColumn = "aggregate_function_param_id",
            targetField = "id", joinTargetColumn = "mix_udaf_param_id"
    )
    private MixUdafParam mixUdafParam;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    @Column(onInsertValue = "0", isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP", onUpdateValue = "CURRENT_TIMESTAMP")
    private Date updateTime;

}
