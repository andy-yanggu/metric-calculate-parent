package com.yanggu.metric_calculate.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "成功"),
    TIME_OUT("408", "请求超时, 请重试"),
    FAIL("500", "服务器异常, 异常信息: {}"),
    TABLE_ID_ERROR("1000", "指标中心没有配置明细宽表, 明细宽表的id: {}"),
    METRIC_CALCULATE_INIT_ERROR("1001", "指标计算类初始化失败"),
    DERIVE_ID_ERROR("1002", "传入的派生指标id: {}有误"),
    ;

    private final String code;

    private final String message;

}
