package com.yanggu.metric_calculate.web.pojo.dto;

import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import lombok.Data;

import java.util.Map;

@Data
public class DeriveData {

    private Long tableId;

    private Map<String, Class<?>> fieldMap;

    private DeriveMetrics deriveMetrics;

}