package com.yanggu.metric_calculate.pojo;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class PutRequest {

    private MetricCube metricCube;

    private JSONObject input;

    private CompletableFuture<DeriveMetricCalculateResult> resultFuture;

}
