package com.yanggu.metric_calculate.flink.pojo;

import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class DeriveConfigData<IN, ACC, OUT> implements Serializable {

    private static final long serialVersionUID = 8555083618866402099L;

    private Long tableId;

    private Map<String, Class<?>> fieldMap;

    /**
     * 自定义udf-jar的路径
     */
    private List<String> aviatorFunctionJarPathList;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

    private Derive derive;

    private transient DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate;

}
