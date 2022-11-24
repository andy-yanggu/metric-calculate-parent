package com.yanggu.handler;

import com.yanggu.error.ErrorCode;
import com.yanggu.error.ErrorCodeException;
import com.yanggu.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();

        if (ex instanceof ErrorCodeException) {
            ErrorCodeException errorCodeException = (ErrorCodeException) ex;
            response.setStatus(errorCodeException.getError().getCode().toString());
            response.setMessage(errorCodeException.getError().getInfo());
            response.setDetailMessage(errorCodeException.getErrorMessage());
        } else {
            response.setStatus(ErrorCode.RULE_MARK_ERROR.getCode().toString());
            response.setMessage(ErrorCode.RULE_MARK_ERROR.getInfo());
            logger.error("Exception : {}", ex);
            response.setDetailMessage(ex.getMessage());
        }

        HttpStatus status = HttpStatus.resolve(500);

        return super.handleExceptionInternal(ex, response, null, status, request);
    }
}
