package com.yanggu.util;

import com.yanggu.error.ErrorCodeException;

public class ApiResponse<T> {

    private String status = "0";
    private String message = "success";
    private T data;

    public ApiResponse() {}

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(ErrorCodeException e) {
        this.status = e.getError().getCode().toString();
        this.message = e.getErrorMessage();
    }

    public ApiResponse<T> set(ErrorCodeException e) {
        this.status = e.getError().getCode().toString();
        this.message = e.getError().getInfo();
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
