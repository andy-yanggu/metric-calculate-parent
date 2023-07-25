package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 字段数据类型
 */
@Getter
@AllArgsConstructor
public enum DataType {

    /**
     * 字符串类型
     */
    STRING(String.class),

    /**
     * 布尔类型
     */
    BOOLEAN(Boolean.class),

    /**
     * 数值型
     */
    LONG(Long.class),

    /**
     * 小数型
     */
    DOUBLE(Double.class),

    /**
     * 字符串集合
     */
    LIST_STRING(List.class),

    /**
     * 布尔集合
     */
    LIST_BOOLEAN(List.class),

    /**
     * 数值集合
     */
    LIST_LONG(List.class),

    /**
     * 小数集合
     */
    LIST_DECIMAL(List.class),
    ;

    /**
     * Java的数据类型
     */
    private final Class<?> type;

}