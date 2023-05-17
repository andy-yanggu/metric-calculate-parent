package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUdafParam;
import lombok.Data;

import java.util.List;

/**
 * 聚合函数参数配置类
 */
@Data
public class AggregateFunctionParam {

    /**
     * 聚合逻辑
     */
    private String calculateLogic;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParam baseUdafParam;

    /**
     * 额外的基本聚合函数参数
     */
    private BaseUdafParam externalBaseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParam mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParam mixUdafParam;

}
