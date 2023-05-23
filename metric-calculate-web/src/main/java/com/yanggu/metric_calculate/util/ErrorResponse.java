package com.yanggu.metric_calculate.util;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status = "500";

    private String message;

    private String detailMessage;

}
