package com.yanggu.metric_calculate.config.service;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;

public interface SimulateMetricCalculateService {

    <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> noStateCalculate(JSONObject input,
                                                                     Integer modelId,
                                                                     Integer deriveId);

    <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> stateCalculate(JSONObject input,
                                                                   Integer modelId,
                                                                   Integer deriveId);
}
