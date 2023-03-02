package com.yanggu.metric_calculate.pojo;

import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.table.Table;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class PutRequest {

    private MetricCube<Table, Long, ?, ?> metricCube;

    private DeriveMetricCalculate deriveMetricCalculate;

    private CompletableFuture<List<DeriveMetricCalculateResult>> resultFuture;

}
