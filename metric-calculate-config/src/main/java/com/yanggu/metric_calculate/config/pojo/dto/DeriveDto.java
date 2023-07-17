package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 派生指标 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeriveDto implements Serializable {

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
    private List<DimensionColumnItem> dimensionList;

    /**
     * 时间字段
     */
    private TimeColumnDto timeColumn;

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
    private Integer includeCurrent;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}