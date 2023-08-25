package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeriveDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2005906604838021242L;

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
    private List<ModelDimensionColumnDto> modelDimensionColumnList;

    /**
     * 时间字段
     */
    private ModelTimeColumnDto modelTimeColumn;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    private AviatorExpressParamDto filterExpressParam;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParamDto aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
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
    private Boolean includeCurrent;

}