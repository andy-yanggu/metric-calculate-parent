package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class DeriveMetricsConfigData implements Serializable {

    @Serial
    private static final long serialVersionUID = 9188722054597361589L;

    /**
     * 派生指标配置类
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