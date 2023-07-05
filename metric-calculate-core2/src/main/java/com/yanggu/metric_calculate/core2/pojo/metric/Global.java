package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 全局指标
 */
@Data
public class Global implements Serializable {

    private static final long serialVersionUID = 2931727545681277544L;

    /**
     * 全局指标id
     */
    private Long id;

    /**
     * 全局指标名称
     */
    private String name;

    /**
     * 全局指标中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 维度字段
     */
    private List<Dimension> dimensionList;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    private AviatorExpressParam filterExpressParam;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParam aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
    private WindowParam windowParam;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

}
