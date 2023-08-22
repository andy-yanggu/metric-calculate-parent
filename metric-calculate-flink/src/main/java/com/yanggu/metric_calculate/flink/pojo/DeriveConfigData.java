package com.yanggu.metric_calculate.flink.pojo;

import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class DeriveConfigData<IN, ACC, OUT> implements Serializable {

    private static final long serialVersionUID = 8555083618866402099L;

    /**
     * 宽表id
     */
    private Long tableId;

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

    /**
     * 派生指标配置类
     */
    private DeriveMetrics deriveMetrics;

    /**
     * 派生指标计算类
     */
    private transient DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate;

}
