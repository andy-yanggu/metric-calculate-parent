package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.pojo.UpdateMetricData;
import com.yanggu.metric_calculate.service.MetricDataService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@Tag(name = "指标数据接口")
@RequestMapping("/metric-data")
public class MetricDataController {

    @Autowired
    private MetricDataService metricDataService;

    @Operation(summary = "查询单个派生指标数据（实时数据）")
    @PostMapping(value = "/query-derive-data", produces = APPLICATION_JSON_VALUE)
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public <IN, ACC, OUT> ApiResponse<DeriveMetricCalculateResult<OUT>> queryDeriveData(
                        @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
                        @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
                        @NotEmpty(message = "维度json数据不能为空") @Parameter(description = "维度json数据", required = true) @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        DeriveMetricCalculateResult<OUT> result = metricDataService.<IN, ACC, OUT>queryDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success(result);
    }

    @Operation(summary = "根据明细查询派生指标实时数据")
    @PostMapping("/query-derive-current-data")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> queryDeriveCurrentData(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotEmpty(message = "派生指标id列表不能为空") @Parameter(description = "派生指标id列表", required = true) @RequestParam List<Long> deriveIdList,
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody JSONObject input) {
        List<DeriveMetricCalculateResult<Object>> list = metricDataService.queryDeriveCurrentData(tableId, deriveIdList, input);
        return ApiResponse.success(list);
    }

    @Operation(summary = "全量填充（计算所有派生指标数据）")
    @PostMapping("/full-fill-derive-data")
    public ApiResponse<Object> fullUpdate(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotEmpty(message = "数据list不能为空") @Parameter(description = "数据list", required = true) @RequestBody List<JSONObject> dataList) {
        metricDataService.fullFillDeriveData(dataList, tableId);
        return ApiResponse.success();
    }

    @Operation(summary = "部分填充（单个派生指标）")
    @PostMapping("/fill-derive-data-by-id")
    public ApiResponse<Object> fillDeriveDataById(
                 @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
                 @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
                 @NotEmpty(message = "数据list不能为空") @Parameter(description = "数据list", required = true) @RequestBody List<JSONObject> dataList) {
        metricDataService.fillDeriveDataById(tableId, deriveId, dataList);
        return ApiResponse.success();
    }

    @Operation(summary = "删除派生指标数据")
    @DeleteMapping("/delete-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public ApiResponse<Object> deleteDeriveData(
                        @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
                        @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
                        @NotEmpty(message = "维度json数据不能为空") @Parameter(description = "维度json数据", required = true) @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        metricDataService.deleteDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success();
    }

    @Operation(summary = "修正派生指标数据")
    @PutMapping("/correct-derive-data")
    public <IN, ACC, OUT> ApiResponse<Object> updateDeriveData(
            @NotNull(message = "维度数据和表数据不能为空") @Parameter(description = "维度数据和表数据", required = true) @RequestBody @Validated UpdateMetricData<IN, ACC, OUT> updateMetricData) {
        metricDataService.correctDeriveData(updateMetricData);
        return ApiResponse.success();
    }

}
