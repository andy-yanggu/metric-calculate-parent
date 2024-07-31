package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.config.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数值型、集合型、对象型聚合函数相关参数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUdafParamVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3591065798650301944L;

    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    private AggregateFunctionVO aggregateFunction;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    private AviatorExpressParamVO metricExpressParam;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    private List<AviatorExpressParamVO> metricExpressParamList;

    /**
     * 聚合函数参数的JSON数据
     */
    private Map<String, Object> param;

}