package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聚合函数类型枚举
 */
@Getter
@AllArgsConstructor
public enum AggregateFunctionTypeEnums {

    /**
     * 数值型
     */
    NUMERICAL,

    /**
     * 集合型
     */
    COLLECTIVE,

    /**
     * 对象型
     */
    OBJECTIVE,

    /**
     * 映射型
     */
    MAP_TYPE,

    /**
     * 混合型
     */
    MIX,
    ;


}
