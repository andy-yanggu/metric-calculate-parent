package com.yanggu.metric_calculate.error;

import lombok.Getter;

@Getter
public class ErrorCodeException extends Exception {

    private final ErrorCode error;
    private final String errorMessage;

    public ErrorCodeException(ErrorCode error) {
        this(error, error.getInfo());
    }

    public ErrorCodeException(ErrorCode error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return getErrorMessage();
    }
}
