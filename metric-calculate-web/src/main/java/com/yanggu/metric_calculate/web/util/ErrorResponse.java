package com.yanggu.metric_calculate.web.util;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status = "500";

    private String message;

    private String detailMessage;

}
