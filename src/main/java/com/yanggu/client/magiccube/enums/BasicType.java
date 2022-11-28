package com.yanggu.client.magiccube.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

import static java.lang.String.format;

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

    private final Class<?> type;

    public static BasicType ofValue(Object value) {
        if (value instanceof Long) {
            return LONG;
        }
        if (value instanceof String) {
            return STRING;
        }
        if (value instanceof Boolean) {
            return BOOLEAN;
        }
        if (value instanceof BigDecimal) {
            return DECIMAL;
        }
        throw new IllegalArgumentException(format("Not support type: %s", value.getClass().getName()));
    }

}
