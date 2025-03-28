package com.yanggu.metric_calculate.web.pojo.dto;

import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DeriveData {

    /**
     * 派生指标配置数据
     */
    private DeriveMetrics deriveMetrics;

    /**
     * 宽表字段
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 自定义udf-jar的路径
     */
    private List<String> aviatorFunctionJarPathList;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

}