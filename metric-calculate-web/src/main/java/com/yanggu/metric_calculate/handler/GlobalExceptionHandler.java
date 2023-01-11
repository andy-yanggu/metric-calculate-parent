package com.yanggu.metric_calculate.handler;

import com.yanggu.metric_calculate.error.ErrorCodeException;
import com.yanggu.metric_calculate.util.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ErrorResponse exceptionHandler(Exception ex) {
        ErrorResponse response = new ErrorResponse();
        ex.printStackTrace();

        if (ex instanceof ErrorCodeException) {
            ErrorCodeException errorCodeException = (ErrorCodeException) ex;
            response.setStatus(errorCodeException.getError().getCode().toString());
            response.setMessage(errorCodeException.getError().getInfo());
            response.setDetailMessage(errorCodeException.getErrorMessage());
        } else {
            response.setDetailMessage(ex.getMessage());
        }
        return response;
    }

}
