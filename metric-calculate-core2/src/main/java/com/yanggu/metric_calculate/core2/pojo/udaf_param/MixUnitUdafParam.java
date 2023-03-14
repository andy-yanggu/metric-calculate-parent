package com.yanggu.metric_calculate.core2.pojo.udaf_param;

import lombok.Data;

import java.util.Map;

/**
 * 混合聚合类型参数
 */
@Data
public class MixUnitUdafParam {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 混合聚合类型定义
     * <p>k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑</p>
     */
    private Map<String, BaseUdafParam> mixAggMap;

    /**
     * 多个聚合值的计算表达式
     */
    private String express;

    /**
     * udaf函数中的参数
     */
    private Map<String, Object> param;

}
