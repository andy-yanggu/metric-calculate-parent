package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 聚合函数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6425099649633730377L;

    /**
     * 主键自增
     */
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
     * 是否内置: 0否, 1是
     */
    private Boolean isBuiltIn;

    /**
     * jar存储id
     */
    private Integer jarStoreId;

    /**
     * 不是内置的聚合函数为外置jar
     */
    private JarStoreVO jarStore;

    /**
     * 聚合函数成员变量
     */
    private List<AggregateFunctionFieldVO> aggregateFunctionFieldList;

}
