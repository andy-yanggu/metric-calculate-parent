package com.yanggu.metric_calculate.flink.pojo;

import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
public class DeriveData<IN, ACC, OUT> implements Serializable {

    private Long tableId;

    private Map<String, Class<?>> fieldMap;

    private Derive derive;

    private transient DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate;

}
