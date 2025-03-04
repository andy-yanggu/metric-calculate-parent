package com.yanggu.metric_calculate.core.pojo.udaf_param;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 映射类型udaf参数
 */
@Data
public class MapUdafParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 4843035217837455297L;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * key的生成逻辑(去重字段列表)
     */
    private List<AviatorExpressParam> distinctFieldParamList;

    /**
     * value的聚合函数参数。只能是数值型、集合型、对象型
     */
    private BaseUdafParam valueAggParam;

    /**
     * 相关参数
     */
    private Map<String, Object> param;

}
