package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamModelColumnRelationTableDef.AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_RELATION;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "成功"),
    FAIL("500", "服务器异常, 异常信息: {}"),
    MODEL_EXIST("10000", "宽表名称或者中文名已经存在"),
    DERIVE_EXIST("10001", "宽表名称或者中文名已经存在"),
    MODEL_COLUMN_NAME_ERROR("10002", "宽表字段名异常"),
    MODEL_COLUMN_EMPTY("10003", "宽表字段为空"),
    MODEL_COLUMN_NAME_DUPLICATE("10004", "宽表字段名重复"),
    MODEL_COLUMN_DISPLAY_NAME_DUPLICATE("10005", "宽表字段中文名重复"),
    AVIATOR_EXPRESS_CHECK_ERROR("10006", "Aviator表达式校验失败"),
    AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR("10007", "Aviator表达式依赖宽表字段错误"),
    ;

    private final String code;

    private final String message;

}
