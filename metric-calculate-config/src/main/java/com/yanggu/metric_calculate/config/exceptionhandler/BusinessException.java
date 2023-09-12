package com.yanggu.metric_calculate.config.exceptionhandler;

import com.yanggu.metric_calculate.config.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.hutool.core.text.StrUtil;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper=false)
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3860832415337462879L;

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