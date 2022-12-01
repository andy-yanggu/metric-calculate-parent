package com.yanggu.client.magiccube.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


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
     * java的数据类型
     */
    private final Class<?> type;

}
