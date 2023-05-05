package com.yanggu.metric_calculate.pojo;


import com.yanggu.metric_calculate.core2.cube.MetricCube;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class QueryRequest {

    private MetricCube metricCube;

    private CompletableFuture<MetricCube> queryFuture;

}
