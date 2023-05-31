package com.yanggu.metric_calculate.controller;

import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.service.MetricConfigDataService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@Api(tags = "指标配置数据接口")
@RequestMapping("/metric-config-data")
public class MetricConfigDataController {

    @Autowired
    private MetricConfigDataService metricConfigDataService;

    @ApiOperation("所有指标配置数据")
    @GetMapping("/all")
    public ApiResponse<List<DataDetailsWideTable>> allMetricConfigData() {
        return ApiResponse.success(metricConfigDataService.allMetricConfigData());
    }

    @ApiOperation("获取某个宽表的指标配置数据")
    @GetMapping("/{tableId}")
    public ApiResponse<DataDetailsWideTable> metricConfigDataById(
            @NotNull(message = "宽表id不能为空") @ApiParam("数据明细宽表id") @PathVariable Long tableId) {
        return ApiResponse.success(metricConfigDataService.metricConfigDataById(tableId));
    }

    @ApiOperation("全量更新指标配置")
    @GetMapping("/refresh")
    public ApiResponse<Object> refreshMetricConfig() {
        metricConfigDataService.buildAllMetric();
        return ApiResponse.success();
    }

    @ApiOperation("增量更新指标配置（更新某个宽表下的所有指标）")
    @PutMapping("/refresh/{tableId}")
    public ApiResponse<Object> updateMetricConfig(
            @NotNull(message = "宽表id不能为空") @ApiParam("数据明细宽表id") @PathVariable Long tableId) {
        metricConfigDataService.updateTable(tableId);
        return ApiResponse.success();
    }

}
