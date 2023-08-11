package com.yanggu.metric_calculate.config.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yanggu.metric_calculate.config.pojo.vo.Result;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

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
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
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
