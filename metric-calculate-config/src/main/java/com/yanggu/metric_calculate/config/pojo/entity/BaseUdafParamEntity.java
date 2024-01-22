package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数值型、集合型、对象型聚合函数相关参数 实体类。
 */
@Data
@Table(value = "base_udaf_param")
@EqualsAndHashCode(callSuper = true)
public class BaseUdafParamEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6528404355516826249L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunctionEntity aggregateFunction;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    @RelationOneToOne(
            joinTable = "base_udaf_param_metric_express_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParamEntity metricExpressParam;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    @RelationOneToMany(
            joinTable = "base_udaf_param_metric_express_list_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParamEntity> metricExpressParamList;

    /**
     * 聚合函数参数的JSON数据
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> param;

}
