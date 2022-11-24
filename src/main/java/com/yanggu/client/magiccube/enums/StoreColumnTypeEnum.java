package com.yanggu.client.magiccube.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标存储宽表字段类型
 */
@Getter
@AllArgsConstructor
public enum StoreColumnTypeEnum {

    /**
     * 维度字段
     */
    DIMENSION(0, "维度"),

    /**
     * 时间字段
     */
    TIME(1, "时间"),

    /**
     * 度量字段
     */
    METRICS(2, "度量");

    private final Integer code;

    private final String desc;

}
