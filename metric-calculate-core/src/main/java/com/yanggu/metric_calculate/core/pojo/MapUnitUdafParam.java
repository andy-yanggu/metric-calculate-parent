package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 映射类型udaf参数
 */
@Data
public class MapUnitUdafParam {

    /**
     * key的生成逻辑(去重字段列表)
     */
    private List<String> distinctFieldList;

    /**
     * value的聚合逻辑
     */
    private String valueAggregateType;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    private NumberObjectCollectionUdafParam valueAggParam;

    private Map<String, Object> param;

}
