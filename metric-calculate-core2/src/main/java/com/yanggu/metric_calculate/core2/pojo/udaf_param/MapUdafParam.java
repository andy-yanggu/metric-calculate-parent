package com.yanggu.metric_calculate.core2.pojo.udaf_param;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 映射类型udaf参数
 */
@Data
public class MapUdafParam {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * key的生成逻辑(去重字段列表)
     */
    private List<String> distinctFieldList;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    private BaseUdafParam valueAggParam;

    /**
     * 相关参数
     */
    private Map<String, Object> param;

}
