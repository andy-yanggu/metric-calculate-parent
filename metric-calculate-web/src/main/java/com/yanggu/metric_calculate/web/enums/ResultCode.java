package com.yanggu.metric_calculate.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "成功"),
    TIME_OUT("408", "请求超时, 请重试"),
    FAIL("500", "服务器异常, 异常信息: {}"),
    ;

    private final String code;

    private final String message;

}
