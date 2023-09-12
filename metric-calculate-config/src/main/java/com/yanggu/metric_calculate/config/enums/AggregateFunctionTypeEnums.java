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

    /**
     * 数值型、集合型和对象型为基本聚合类型
     *
     * @param enums
     * @return
     */
    public static boolean isBasicType(AggregateFunctionTypeEnums enums) {
        return NUMERICAL.equals(enums) || COLLECTIVE.equals(enums) || OBJECTIVE.equals(enums);
    }

}
