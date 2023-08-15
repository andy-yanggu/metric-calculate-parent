package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 映射类型udaf参数 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "map_udaf_param")
public class MapUdafParam implements Serializable {

    private static final long serialVersionUID = -7009613660443536653L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunction aggregateFunction;

    /**
     * key的生成逻辑(去重字段列表)
     */
    @RelationOneToMany(
            joinTable = "map_udaf_param_distinct_field_list_relation",
            selfField = "id", joinSelfColumn = "map_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParam> distinctFieldParamList;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    @RelationOneToOne(
            joinTable = "map_udaf_param_value_agg_relation",
            selfField = "id", joinSelfColumn = "map_udaf_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private BaseUdafParam valueAggParam;

    /**
     * Aviator函数参数的JSON数据
     */
    private String param;

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
