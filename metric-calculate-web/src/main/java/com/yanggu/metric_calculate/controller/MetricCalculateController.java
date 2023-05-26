package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.service.MetricCalculateService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "指标计算接口")
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    @Autowired
    private MetricCalculateService metricCalculateService;

    @ApiOperation("有状态-计算接口")
    @PostMapping("/state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> stateExecute(
                                                            @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateExecute(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> noStateExecute(
                                                            @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateExecute(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口（攒批查询）")
    @PostMapping("/no-state-calculate-accumulate-batch")
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateExecuteAccumulateBatch(
                                                                @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        return metricCalculateService.noStateExecuteAccumulateBatch(input);
    }

    @ApiOperation("有状态-计算接口（攒批查询和攒批更新）")
    @PostMapping("/state-calculate-accumulate-batch")
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> stateExecuteAccumulateBatch(
                                                             @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        return metricCalculateService.stateExecuteAccumulateBatch(input);
    }

}
