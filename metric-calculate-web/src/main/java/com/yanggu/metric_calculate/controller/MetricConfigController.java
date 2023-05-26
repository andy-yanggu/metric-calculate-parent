package com.yanggu.metric_calculate.controller;

import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.service.MetricConfigService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 指标配置接口
 */
@Slf4j
@RestController
@Api(tags = "指标配置接口")
@RequestMapping("/metric-config")
public class MetricConfigController {

    @Autowired
    private MetricConfigService metricConfigService;

    @ApiOperation("所有指标配置数据")
    @GetMapping("/all-metric-config-data")
    public ApiResponse<List<DataDetailsWideTable>> allMetricConfigData() {
        return ApiResponse.success(metricConfigService.allMetricConfigData());
    }

    @ApiOperation("全量更新指标配置")
    @GetMapping("/refresh-metric-config")
    public ApiResponse<Object> refreshMetricConfig() {
        metricConfigService.buildAllMetric();
        return ApiResponse.success();
    }

    @ApiOperation("增量更新指标配置（更新某个宽表下的所有指标）")
    @PutMapping("/update-metric-config")
    public ApiResponse<Object> updateMetricConfig(@ApiParam("数据明细宽表id") @RequestParam Long tableId) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        metricConfigService.updateTable(tableId);
        return apiResponse;
    }

}
