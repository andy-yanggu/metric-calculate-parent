package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import com.yanggu.metric_calculate.config.enums.AccuracyEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
@Table(value = "derive")
@EqualsAndHashCode(callSuper = true)
public class DeriveEntity extends BaseUserEntity implements Serializable {

    @Serial
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
     * 原子指标id
     */
    private Integer atomId;

    /**
     * 原子指标
     */
    @RelationManyToOne(selfField = "atomId", targetField = "id")
    private AtomEntity atom;

    /**
     * 维度字段
     */
    @RelationManyToMany(
            joinTable = "derive_model_dimension_column_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "model_dimension_column_id", orderBy = "sort")
    private List<ModelDimensionColumnEntity> modelDimensionColumnList;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    @RelationOneToOne(
            joinTable = "derive_filter_express_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private AviatorExpressParamEntity filterExpressParam;

    /**
     * 窗口相关参数
     */
    @RelationOneToOne(
            joinTable = "derive_window_param_relation",
            selfField = "id", joinSelfColumn = "derive_id",
            targetField = "id", joinTargetColumn = "window_param_id"
    )
    private WindowParamEntity windowParam;

    /**
     * 是否包含当前笔
     */
    private Boolean includeCurrent;

    /**
     * 计量单位
     */
    private String unitMeasure;

    /**
     * 精度类型(0不处理 1四舍五入 2向上保留)
     */
    private AccuracyEnum roundAccuracyType;

    /**
     * 精度长度
     */
    private Integer roundAccuracyLength;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 目录编码
     */
    private String directoryCode;

}
