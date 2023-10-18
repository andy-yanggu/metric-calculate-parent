package com.yanggu.metric_calculate.web.exceptionhandler;

import com.yanggu.metric_calculate.web.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.text.StrUtil;

@Data
@EqualsAndHashCode(callSuper = true)
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