package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.base.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateFunctionParamVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6999604291880819075L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    /**
     * 聚合函数
     */
    private AggregateFunctionVO aggregateFunction;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParamVO baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParamVO mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParamVO mixUdafParam;

}
