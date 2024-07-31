package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 映射类型udaf参数 实体类。
 */
@Data
@Table(value = "map_udaf_param")
@EqualsAndHashCode(callSuper = true)
public class MapUdafParamEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7009613660443536653L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunctionEntity aggregateFunction;

    /**
     * key的生成逻辑(去重字段列表)
     */
    @RelationOneToMany(
            joinTable = "map_udaf_param_distinct_field_list_relation",
            selfField = "id", joinSelfColumn = "map_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParamEntity> distinctFieldParamList;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    @RelationOneToOne(
            joinTable = "map_udaf_param_value_agg_relation",
            selfField = "id", joinSelfColumn = "map_udaf_param_id",
            targetField = "id", joinTargetColumn = "base_udaf_param_id"
    )
    private BaseUdafParamEntity valueAggParam;

    /**
     * 聚合函数参数的JSON数据
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> param;

}
