package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "成功"),
    FAIL("500", "服务器异常, 异常信息: {}"),
    MODEL_EXIST("10000", "宽表名称或者中文名已经存在"),
    DERIVE_EXIST("10001", "宽表名称或者中文名已经存在"),
    MODEL_COLUMN_NAME_ERROR("10002", "宽表字段名异常"),
    MODEL_COLUMN_EMPTY("10003", "宽表字段为空"),
    ;

    private final String code;

    private final String message;

}
