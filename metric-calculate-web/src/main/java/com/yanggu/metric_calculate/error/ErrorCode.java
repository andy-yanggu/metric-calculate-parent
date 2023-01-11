package com.yanggu.metric_calculate.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ;

    private final Integer httpStatus;

    private final Integer code;

    private final String info;

    private final String format;

    ErrorCode(Integer httpStatus, Integer code, String info, String format) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.info = info;
        this.format = format;
    }

    public ErrorCodeException formatAsException(Object... args) {
        String errorMessage;
        if (args.length == 0 && format == null) {
            errorMessage = null;
        } else if (format == null) {
            errorMessage =
                    String.format(
                            "No formation for error type %s", this.getClass().getCanonicalName());
        } else {
            errorMessage = String.format(format, args);
        }
        return new ErrorCodeException(this, errorMessage);
    }

}
