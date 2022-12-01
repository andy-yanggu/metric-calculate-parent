package com.yanggu.client.magiccube.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccuracyEnum {

    /**
     * 不处理
     */
    NOT_HANDLE(0, "不处理"),

    /**
     * 四舍五入
     */
    ROUNDING(1, "四舍五入"),

    /**
     * 向上保留
     */
    KEEP_UP(2, "向上保留");

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 精度处理逻辑
     */
    private final String desc;

}
