package com.yanggu.metric_calculate.config.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yanggu.metric_calculate.config.service.SimulateMetricCalculateService;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Tag(name = "仿真指标计算")
@RequestMapping("/simulate-metric-calculate")
public class SimulateMetricCalculateController {

    @Autowired
    private SimulateMetricCalculateService simulateMetricCalculateService;

    @ApiOperationSupport(order = 1)
    @Operation(summary = "无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> noStateCalculate(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input,
            @RequestParam Integer modelId, @RequestParam Integer deriveId) {
        return simulateMetricCalculateService.<IN, ACC, OUT>noStateCalculate(input, modelId, deriveId);
    }

    @ApiOperationSupport(order = 2)
    @Operation(summary = "有状态-计算接口")
    @PostMapping("/state-calculate")
    public <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> stateCalculate(
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody Map<String, Object> input,
            @RequestParam Integer modelId, @RequestParam Integer deriveId) {
        return simulateMetricCalculateService.<IN, ACC, OUT>stateCalculate(input, modelId, deriveId);
    }

}
