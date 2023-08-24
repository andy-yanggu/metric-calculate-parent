package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aggregate_function")
public class AggregateFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -2251996364508407313L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 唯一标识
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * 聚合函数类型（数值、集合、对象、混合、映射）
     */
    private AggregateFunctionTypeEnums type;

    /**
     * 集合型和对象型主键策略（0没有主键、1去重字段、2排序字段、3比较字段）
     */
    private Integer keyStrategy;

    /**
     * 集合型和对象型保留字段策略（0不保留任何数据、1保留指定字段、2保留原始数据）
     */
    private Integer retainStrategy;

    /**
     * 数值型是否需要多个参数（0否，1是需要多个例如协方差）
     */
    private Boolean multiNumber;

    /**
     * 聚合函数成员变量
     */
    @RelationOneToMany(selfField = "id", targetField = "aggregateFunctionId")
    private List<AggregateFunctionField> aggregateFunctionFieldList;

}
