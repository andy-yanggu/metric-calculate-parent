package com.yanggu.metric_calculate.config.pojo.vo;

import com.yanggu.metric_calculate.config.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6826494148838485626L;

    private boolean success;

    private T data;

    private String code;

    private String message;

    private Result(boolean success, T data, String code, String message) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
    }

    private Result(boolean success, T data, ResultCode resultCode) {
        this.success = success;
        this.data = data;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> ok() {
        return new Result<>(Boolean.TRUE, null, ResultCode.SUCCESS);
    }

    /**
     * 成功返回
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(Boolean.TRUE, data, ResultCode.SUCCESS);
    }

    /**
     * 异常返回-指定错误码
     */
    public static Result<Void> fail(ResultCode resultCode) {
        return new Result<>(Boolean.FALSE, null, resultCode);
    }

    /**
     * 异常返回-非指定异常
     */
    public static Result<Void> fail(String code, String message) {
        return new Result<>(Boolean.FALSE, null, code, message);
    }

}