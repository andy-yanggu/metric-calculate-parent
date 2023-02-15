package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class MixUnitUdafParam {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑
     */
    private Map<String, BaseUdafParam> mixAggMap;

    /**
     * 多个聚合值的计算表达式
     */
    private String express;

    private Map<String, Object> param;

}
