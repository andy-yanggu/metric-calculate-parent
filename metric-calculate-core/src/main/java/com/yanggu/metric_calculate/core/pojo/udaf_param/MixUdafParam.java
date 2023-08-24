package com.yanggu.metric_calculate.core.pojo.udaf_param;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
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
     * 多个基本聚合函数参数
     */
    private List<MixUdafParamItem> mixUdafParamItemList;

    /**
     * 多个聚合值的计算表达式
     */
    private AviatorExpressParam metricExpressParam;

    /**
     * udaf函数中的参数
     */
    private Map<String, Object> param;

}
