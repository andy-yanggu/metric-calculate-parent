package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数值型、集合型、对象型聚合函数相关参数 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "base_udaf_param")
public class BaseUdafParam implements Serializable {

    private static final long serialVersionUID = 6528404355516826249L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    @RelationOneToOne(selfField = "aggregateFunctionId", targetField = "id")
    private AggregateFunction aggregateFunction;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    @RelationOneToOne(
            joinTable = "base_udaf_param_metric_express_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam metricExpressParam;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    @RelationOneToOne(
            joinTable = "base_udaf_param_metric_express_list_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParam> metricExpressParamList;

    /**
     * 保留字段表达式（retainExpress）：对象型和集合型只保留指定字段的值
     */
    @RelationOneToOne(
            joinTable = "base_udaf_param_retain_express_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam retainExpressParam;

    /**
     * 对象型比较字段列表(对象型最大对象、最小对象)
     */
    @RelationOneToMany(
            joinTable = "base_udaf_param_objective_compare_field_express_list_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParam> objectiveCompareFieldParamList;

    /**
     * 排序字段列表（sortFieldList）：类似SQL中的ORDER BY id ASC, user_name DESC，多字段排序。
     * <p>对象型（最大对象、最小对象）、集合型（排序列表）</p>
     */
    @RelationOneToMany(
            joinTable = "base_udaf_param_collective_sort_field_list_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "field_order_param_id"
    )
    private List<FieldOrderParam> collectiveSortFieldList;

    /**
     * 去重字段列表（distinctFieldList）：根据多个字段进行去重。集合型（去重列表）
     */
    @RelationOneToMany(
            joinTable = "base_udaf_param_distinct_field_list_relation",
            selfField = "id", joinSelfColumn = "base_udaf_param_id",
            targetField = "id", joinTargetColumn = "field_order_param_id"
    )
    private List<AviatorExpressParam> distinctFieldListParamList;

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
