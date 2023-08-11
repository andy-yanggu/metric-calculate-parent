package com.yanggu.metric_calculate.web.pojo.dto;

import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import lombok.Data;

import java.util.Map;

@Data
public class DeriveData {

    private Long tableId;

    private Map<String, Class<?>> fieldMap;

    private Derive derive;

}