package com.yanggu.metric_calculate.web.pojo;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class PutRequest {

    private MetricCube metricCube;

    private JSONObject input;

    private CompletableFuture<DeriveMetricCalculateResult> resultFuture;

}
