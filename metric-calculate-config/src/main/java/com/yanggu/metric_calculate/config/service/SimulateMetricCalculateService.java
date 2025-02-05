package com.yanggu.metric_calculate.config.service;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;

import java.util.Map;

public interface SimulateMetricCalculateService {

    <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> noStateCalculate(Map<String, Object> input,
                                                                     Integer modelId,
                                                                     Integer deriveId);

    <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> stateCalculate(Map<String, Object> input,
                                                                   Integer modelId,
                                                                   Integer deriveId);
}
