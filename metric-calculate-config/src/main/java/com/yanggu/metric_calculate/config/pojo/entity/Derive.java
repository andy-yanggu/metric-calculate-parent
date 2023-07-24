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
 * 派生指标 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "derive")
public class Derive implements Serializable {

    private static final long serialVersionUID = 2470923557083328784L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 名称
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
     * 宽表id
     */
    private Integer modelId;

    /**
     * 维度字段
     */
    @RelationManyToMany(
            joinTable = "derive_dimension_column_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "dimension_column_id")
    private List<ModelDimensionColumn> modelDimensionColumnList;

    /**
     * 时间字段
     */
    @RelationManyToOne(
            joinTable = "derive_time_column_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "time_column_id"
    )
    private ModelTimeColumn modelTimeColumn;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    @RelationOneToOne(
            joinTable = "derive_filter_express_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParam filterExpressParam;

    /**
     * 聚合函数参数
     */
    @RelationOneToOne(
            joinTable = "derive_aggregate_function_param_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "aggregate_function_param_id"
    )
    private AggregateFunctionParam aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
    @RelationOneToOne(
            joinTable = "derive_window_param_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "window_param_id"
    )
    private WindowParam windowParam;

    /**
     * 计量单位
     */
    private String unitMeasure;

    /**
     * 精度
     */
    private Integer roundAccuracy;

    /**
     * 精度类型(0不处理 1四舍五入 2向上保留)
     */
    private Integer roundAccuracyType;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 目录编码
     */
    private String directoryCode;

    /**
     * 是否包含当前笔
     */
    private Integer includeCurrent;

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
