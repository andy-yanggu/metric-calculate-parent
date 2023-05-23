package com.yanggu.metric_calculate.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {

    private String status = "0";

    private String message = "success";

    private T data;

    public static ApiResponse<Object> success() {
        return new ApiResponse<>();
    }

}
