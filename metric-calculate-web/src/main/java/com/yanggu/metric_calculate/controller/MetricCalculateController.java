package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.service.MetricCalculateService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@Api(tags = "指标计算接口")
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    @Autowired
    private MetricCalculateService metricCalculateService;

    @ApiOperation("无状态-计算接口（多线程）")
    @PostMapping("/no-state-calculate/thread")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> noStateExecute(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateExecuteThread(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口（批查询）")
    @PostMapping("/no-state-calculate/batch")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> noStateExecuteBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateExecuteBatch(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口（内存攒批查询）")
    @PostMapping("/no-state-calculate/accumulate-batch")
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateExecuteAccumulateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        return metricCalculateService.noStateExecuteAccumulateBatch(input);
    }

    @ApiOperation("有状态-计算接口（多线程）")
    @PostMapping("/state-calculate/thread")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> stateExecuteThread(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateExecuteThread(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("有状态-计算接口（批处理）")
    @PostMapping("/state-calculate/thread")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> stateExecute(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateExecuteBatch(input);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("有状态-计算接口（内存攒批查询和攒批更新）")
    @PostMapping("/state-calculate/accumulate-batch")
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> stateExecuteAccumulateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        return metricCalculateService.stateExecuteAccumulateBatch(input);
    }

}
