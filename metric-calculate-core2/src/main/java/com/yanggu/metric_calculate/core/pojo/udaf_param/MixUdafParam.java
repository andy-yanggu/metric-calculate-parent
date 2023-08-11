package com.yanggu.metric_calculate.core.pojo.udaf_param;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 混合聚合类型参数
 */
@Data
public class MixUdafParam implements Serializable {

    private static final long serialVersionUID = 6154062766089115154L;

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
    private AviatorExpressParam metricExpressParam;

    /**
     * udaf函数中的参数
     */
    private Map<String, Object> param;

}
