package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 混合类型udaf参数 实体类。
 */
@Data
@Table(value = "mix_udaf_param")
@EqualsAndHashCode(callSuper = true)
public class MixUdafParamEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -827357754476085192L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunctionEntity aggregateFunction;

    /**
     * 混合聚合类型定义
     * <p>k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑</p>
     */
    @RelationOneToMany(selfField = "id", targetField = "mixUdafParamId", orderBy = "sort")
    private List<MixUdafParamItemEntity> mixUdafParamItemList;

    /**
     * 多个聚合值的计算表达式
     */
    @RelationOneToOne(
            joinTable = "mix_udaf_param_metric_express_relation",
            selfField = "id", joinSelfColumn = "mix_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParamEntity metricExpressParam;

    /**
     * 聚合函数参数的JSON数据
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> param;

}
