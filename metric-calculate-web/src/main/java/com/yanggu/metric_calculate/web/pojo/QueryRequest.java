package com.yanggu.metric_calculate.web.pojo;


import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class QueryRequest {

    private DimensionSet dimensionSet;

    private CompletableFuture<MetricCube> queryFuture;

}
