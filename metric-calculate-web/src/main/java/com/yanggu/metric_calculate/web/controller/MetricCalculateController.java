package com.yanggu.metric_calculate.web.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.web.pojo.vo.Result;
import com.yanggu.metric_calculate.web.service.MetricCalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@Tag(name = "指标计算接口")
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    @Autowired
    private MetricCalculateService metricCalculateService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "无状态-计算接口（多线程）")
    @PostMapping("/no-state-calculate/thread")
    public Result<List<DeriveMetricCalculateResult<Object>>> noStateCalculateThread(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateCalculateThread(input);
        return Result.ok(resultList);
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "无状态-计算接口（批查询）")
    @PostMapping("/no-state-calculate/batch")
    public Result<List<DeriveMetricCalculateResult<Object>>> noStateCalculateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) throws Exception {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateCalculateBatch(input);
        return Result.ok(resultList);
    }

    @ApiOperationSupport(order = 3)
    @Operation(summary = "无状态-计算接口（内存攒批查询）")
    @PostMapping("/no-state-calculate/accumulate-batch")
    public DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> noStateCalculateAccumulateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) {
        return metricCalculateService.noStateCalculateAccumulateBatch(input);
    }

    @ApiOperationSupport(order = 4)
    @Operation(summary = "有状态-计算接口（多线程）")
    @PostMapping("/state-calculate/thread")
    public Result<List<DeriveMetricCalculateResult<Object>>> stateCalculateThread(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateCalculateThread(input);
        return Result.ok(resultList);
    }

    @ApiOperationSupport(order = 5)
    @Operation(summary = "有状态-计算接口（批处理）")
    @PostMapping("/state-calculate/batch")
    public Result<List<DeriveMetricCalculateResult<Object>>> stateCalculateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) throws Exception {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateCalculateBatch(input);
        return Result.ok(resultList);
    }

    @ApiOperationSupport(order = 6)
    @Operation(summary = "有状态-计算接口（内存攒批查询和攒批更新）")
    @PostMapping("/state-calculate/accumulate-batch")
    public DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> stateCalculateAccumulateBatch(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input) {
        return metricCalculateService.stateCalculateAccumulateBatch(input);
    }

}
