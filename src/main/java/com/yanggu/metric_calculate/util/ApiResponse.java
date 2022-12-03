package com.yanggu.metric_calculate.util;

import com.yanggu.metric_calculate.error.ErrorCodeException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {

    private String status = "0";
    private String message = "success";
    private T data;

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

}
