package com.yanggu.metric_calculate.web.handler;

import com.yanggu.metric_calculate.web.error.ErrorCodeException;
import com.yanggu.metric_calculate.web.util.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse exceptionHandler(Exception ex) {
        ErrorResponse response = new ErrorResponse();

        if (ex instanceof ErrorCodeException) {
            ErrorCodeException errorCodeException = (ErrorCodeException) ex;
            response.setStatus(errorCodeException.getError().getCode().toString());
            response.setMessage(errorCodeException.getError().getInfo());
            response.setDetailMessage(errorCodeException.getErrorMessage());
            log.info("发生业务异常: " + errorCodeException.getErrorMessage(), errorCodeException);
        } else {
            response.setDetailMessage(ex.getMessage());
            log.warn("发生运行时异常: " + ex.getMessage(), ex);
        }
        return response;
    }

}
