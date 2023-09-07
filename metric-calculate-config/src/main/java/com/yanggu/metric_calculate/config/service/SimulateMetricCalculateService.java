package com.yanggu.metric_calculate.config.service;


import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import org.dromara.hutool.json.JSONObject;

public interface SimulateMetricCalculateService {

    <R> DeriveMetricCalculateResult<R> noStateCalculate(JSONObject input,
                                                        Integer modelId,
                                                        Integer deriveId) throws Exception;

    <R> DeriveMetricCalculateResult<R> stateCalculate(JSONObject input,
                                                      Integer modelId,
                                                      Integer deriveId);
}
