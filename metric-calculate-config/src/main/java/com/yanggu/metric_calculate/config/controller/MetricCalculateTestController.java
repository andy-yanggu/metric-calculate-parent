package com.yanggu.metric_calculate.config.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 指标计算测试
 */
@RestController
@Tag(name = "指标计算测试")
@RequestMapping("/metric-calculate-test")
public class MetricCalculateTestController {

    @ApiOperationSupport(order = 1)
    @Operation(summary = "无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public Result<List<DeriveMetricCalculateResult<Object>>> noStateCalculateThread(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody JSONObject input) {
        //List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateCalculateThread(input);
        //return Result.ok(resultList);
        return null;
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "有状态-计算接口")
    @PostMapping("/state-calculate")
    public Result<List<DeriveMetricCalculateResult<Object>>> stateCalculateThread(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody JSONObject input) {
        //List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateCalculateThread(input);
        //return Result.ok(resultList);
        return null;
    }

}
