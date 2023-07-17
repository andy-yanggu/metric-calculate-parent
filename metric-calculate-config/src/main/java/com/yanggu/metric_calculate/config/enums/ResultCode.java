package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS("200", "成功"),

    /**
     * 失败
     */
    FAIL("500", "服务器异常, 异常信息: {}"),

    MODEL_EXIST("10000", "宽表名称或者中文名已经存在"),
    ;

    private final String code;

    private final String message;

}
