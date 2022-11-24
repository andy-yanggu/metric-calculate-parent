package com.yanggu.error;

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

    public ErrorCode getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getMessage() {
        return getErrorMessage();
    }
}
