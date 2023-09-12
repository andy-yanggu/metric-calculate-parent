package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.MixUdafParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数配置类
 */
@Data
public class AggregateFunctionParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 474654595932207481L;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParam baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParam mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParam mixUdafParam;

}
