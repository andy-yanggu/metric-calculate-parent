package com.yanggu.metric_calculate.config.exceptionhandler;

import com.yanggu.metric_calculate.config.enums.ResultCode;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {"com.yanggu.metric_calculate.config.controller"})
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handlerBusinessException(BusinessException exception) {
        log.warn("[业务异常] 异常code: {}, 异常信息: {}", exception.getCode(), exception.getMessage());
        return Result.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception exception, HttpServletRequest request) {
        log.error("[运行时异常] 请求url: {}, 请求方式: {}, 异常信息: {}", request.getRequestURI(), request.getMethod(), exception.getMessage());
        exception.printStackTrace();
        return Result.fail(ResultCode.FAIL);
    }

}
