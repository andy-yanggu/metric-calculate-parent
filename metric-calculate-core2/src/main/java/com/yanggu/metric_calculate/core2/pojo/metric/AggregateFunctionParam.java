package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.ChainPattern;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MapUnitUdafParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.MixUnitUdafParam;
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
     * 映射类型聚合函数参数
     */
    private MapUnitUdafParam mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUnitUdafParam mixUnitUdafParam;

    /**
     * 是否是CEP类型
     */
    private Boolean isCep;

    /**
     * CEP, 事件模式配置数据
     */
    private ChainPattern chainPattern;

}
