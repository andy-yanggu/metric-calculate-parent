package com.yanggu.metric_calculate.core2.util;


import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class QueryRequest {

    private DimensionSet dimensionSet;

    private CompletableFuture<MetricCube> queryFuture;

}
