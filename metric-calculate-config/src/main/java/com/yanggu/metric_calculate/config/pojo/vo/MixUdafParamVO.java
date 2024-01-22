package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.base.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 混合类型udaf参数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MixUdafParamVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8847014730737382072L;

    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    private AggregateFunctionVO aggregateFunction;

    /**
     * 混合聚合类型定义
     * <p>k是名字, value是基本聚合类型的参数, 用于定义聚合计算逻辑</p>
     */
    private List<MixUdafParamItemVO> mixUdafParamItemList;

    /**
     * 多个聚合值的计算表达式
     */
    private AviatorExpressParamVO metricExpressParam;

    /**
     * 聚合函数参数的JSON数据
     */
    private Map<String, Object> param;

}