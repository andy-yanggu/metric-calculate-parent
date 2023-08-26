package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 映射类型udaf参数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MapUdafParamDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -3797757638074327166L;

    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    private AggregateFunctionDto aggregateFunction;

    /**
     * key的生成逻辑(去重字段列表)
     */
    private List<AviatorExpressParamDto> distinctFieldParamList;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    private BaseUdafParamDto valueAggParam;

    /**
     * 聚合函数参数的JSON数据
     */
    private Map<String, Object> param;

}