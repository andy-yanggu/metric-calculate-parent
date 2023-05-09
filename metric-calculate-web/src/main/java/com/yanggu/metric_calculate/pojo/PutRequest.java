package com.yanggu.metric_calculate.pojo;

import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class PutRequest {

    private MetricCube metricCube;

    private DeriveMetricCalculate deriveMetricCalculate;

    private CompletableFuture<List<DeriveMetricCalculateResult>> resultFuture;

}
