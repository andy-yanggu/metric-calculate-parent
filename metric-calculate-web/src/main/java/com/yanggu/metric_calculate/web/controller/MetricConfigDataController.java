package com.yanggu.metric_calculate.web.controller;

import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.web.pojo.vo.Result;
import com.yanggu.metric_calculate.web.service.MetricConfigDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@Tag(name = "指标配置数据接口")
@RequestMapping("/metric-config-data")
public class MetricConfigDataController {

    @Autowired
    private MetricConfigDataService metricConfigDataService;

    @GetMapping("/all")
    @Operation(summary = "所有指标配置数据")
    public Result<List<Model>> allMetricConfigData() {
        return Result.ok(metricConfigDataService.allMetricConfigData());
    }

    @GetMapping("/{tableId}")
    @Operation(summary = "获取某个宽表的指标配置数据")
    public Result<Model> metricConfigDataById(
            @NotNull(message = "宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @PathVariable("tableId") Long tableId) {
        return Result.ok(metricConfigDataService.metricConfigDataById(tableId));
    }

    @GetMapping("/refresh")
    @Operation(summary = "全量更新指标配置")
    public Result<Void> refreshMetricConfig() {
        metricConfigDataService.buildAllMetric();
        return Result.ok();
    }

    @PutMapping("/refresh/{tableId}")
    @Operation(summary = "增量更新指标配置（更新某个宽表下的所有指标）")
    public Result<Void> updateMetricConfig(
            @NotNull(message = "宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @PathVariable("tableId") Long tableId) {
        metricConfigDataService.updateTable(tableId);
        return Result.ok();
    }

}
