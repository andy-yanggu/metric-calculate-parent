package com.yanggu.metric_calculate.config.exceptionhandler;

import com.yanggu.metric_calculate.config.pojo.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.yanggu.metric_calculate.config.enums.ResultCode.FAIL;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handlerBusinessException(BusinessException exception, HttpServletRequest request) {
        log.warn("[业务异常] 请求url: {}, 请求方式: {}, 异常code: {}, 异常信息: {}", request.getRequestURI(), request.getMethod(), exception.getCode(), exception.getMessage());
        return Result.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception exception, HttpServletRequest request) {
        log.error("[运行时异常] 请求url: {}, 请求方式: {}, 异常信息: {}", request.getRequestURI(), request.getMethod(), exception.getMessage(), exception);
        return Result.fail(FAIL.getCode(), exception.getMessage());
    }

}
