package com.yanggu.metric_calculate.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 数据明细宽表字段数据类型
 */
@Getter
@AllArgsConstructor
public enum BasicType {

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
    DECIMAL(BigDecimal.class);

    /**
     * Java的数据类型
     */
    private final Class<?> type;

}
