package com.yanggu.metric_calculate.web.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ;

    private final Integer httpStatus;

    private final Integer code;

    private final String info;

    private final String format;

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
