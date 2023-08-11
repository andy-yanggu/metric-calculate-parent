package com.yanggu.metric_calculate.config.pojo.exception;

import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.config.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessException extends RuntimeException {

    private final String code;

    private final String message;

    public BusinessException(String code, String message, Object... params) {
        super(StrUtil.format(message, params));
        this.code = code;
        this.message = StrUtil.format(message, params);
    }

    public BusinessException(ResultCode resultCode, Object... params) {
        super(StrUtil.format(resultCode.getMessage(), params));
        this.code = resultCode.getCode();
        this.message = StrUtil.format(resultCode.getMessage(), params);
    }

}