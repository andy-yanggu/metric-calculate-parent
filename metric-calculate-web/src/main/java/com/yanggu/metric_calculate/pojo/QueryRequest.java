package com.yanggu.metric_calculate.pojo;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.table.Table;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class QueryRequest {

    private MetricCube<Table, Long, ?, ?> metricCube;

    private CompletableFuture<MetricCube<Table, Long, ?, ?>> queryFuture;

}
