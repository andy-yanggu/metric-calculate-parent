package com.yanggu.metric_calculate.config.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 混合类型udaf参数 实体类。
 */
@Data
public class MixUdafParamDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8847014730737382072L;

    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    private AggregateFunctionDTO aggregateFunction;

    /**
     * 混合聚合类型定义
     * <p>k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑</p>
     */
    private List<MixUdafParamItemDTO> mixUdafParamItemList;

    /**
     * 多个聚合值的计算表达式
     */
    private AviatorExpressParamDTO metricExpressParam;

    /**
     * 聚合函数参数的JSON数据
     */
    private Map<String, Object> param;

}