package com.yanggu.metric_calculate.config.pojo.dto;

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
public class BaseUdafParamDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 3591065798650301944L;

    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    private AggregateFunctionDto aggregateFunction;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    private AviatorExpressParamDto metricExpressParam;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    private List<AviatorExpressParamDto> metricExpressParamList;

    /**
     * 保留字段表达式（retainExpress）：对象型和集合型只保留指定字段的值
     */
    private AviatorExpressParamDto retainExpressParam;

    /**
     * 对象型比较字段列表(对象型最大对象、最小对象)
     */
    private List<AviatorExpressParamDto> objectiveCompareFieldParamList;

    /**
     * 排序字段列表（sortFieldList）：类似SQL中的ORDER BY id ASC, user_name DESC，多字段排序。
     * <p>集合型（排序列表）</p>
     */
    private List<FieldOrderParamDto> collectiveSortFieldList;

    /**
     * 去重字段列表（distinctFieldList）：根据多个字段进行去重。集合型（去重列表）
     */
    private List<AviatorExpressParamDto> distinctFieldListParamList;

    /**
     * 聚合函数参数的JSON数据
     */
    private Map<String, Object> param;

}