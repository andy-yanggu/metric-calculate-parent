package com.yanggu.metric_calculate.web.controller;

import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.web.pojo.dto.UpdateMetricData;
import com.yanggu.metric_calculate.web.pojo.vo.Result;
import com.yanggu.metric_calculate.web.service.MetricDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.dromara.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@Validated
@RestController
@Tag(name = "指标数据接口")
@RequestMapping("/metric-data")
public class MetricDataController {

    @Autowired
    private MetricDataService metricDataService;

    @Operation(summary = "查询单个派生指标数据（实时数据）")
    @PostMapping("/query-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public <IN, ACC, OUT> Result<DeriveMetricCalculateResult<OUT>> queryDeriveData(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
            @NotEmpty(message = "维度json数据不能为空") @Parameter(description = "维度json数据", required = true) @RequestBody LinkedHashMap<String, Object> dimensionMap) throws Exception {
        DeriveMetricCalculateResult<OUT> result = metricDataService.<IN, ACC, OUT>queryDeriveData(tableId, deriveId, dimensionMap);
        return Result.ok(result);
    }

    @Operation(summary = "根据明细查询派生指标实时数据")
    @PostMapping("/query-derive-current-data")
    public Result<List<DeriveMetricCalculateResult<Object>>> queryDeriveCurrentData(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotEmpty(message = "派生指标id列表不能为空") @Parameter(description = "派生指标id列表", required = true) @RequestParam List<Long> deriveIdList,
            @NotEmpty(message = "明细宽表数据不能为空") @Parameter(description = "明细宽表数据", required = true) @RequestBody JSONObject input) throws Exception {
        List<DeriveMetricCalculateResult<Object>> list = metricDataService.queryDeriveCurrentData(tableId, deriveIdList, input);
        return Result.ok(list);
    }

    @Operation(summary = "全量填充（计算所有派生指标数据）")
    @PostMapping("/full-fill-derive-data")
    public Result<Void> fullUpdate(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotEmpty(message = "数据list不能为空") @Parameter(description = "数据list", required = true) @RequestBody List<JSONObject> dataList) throws Exception {
        metricDataService.fullFillDeriveData(dataList, tableId);
        return Result.ok();
    }

    @Operation(summary = "部分填充（单个派生指标）")
    @PostMapping("/fill-derive-data-by-id")
    public Result<Void> fillDeriveDataById(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
            @NotEmpty(message = "数据list不能为空") @Parameter(description = "数据list", required = true) @RequestBody List<JSONObject> dataList) throws Exception {
        metricDataService.fillDeriveDataById(tableId, deriveId, dataList);
        return Result.ok();
    }

    @Operation(summary = "删除派生指标数据")
    @DeleteMapping("/delete-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public Result<Void> deleteDeriveData(
            @NotNull(message = "数据明细宽表id不能为空") @Parameter(description = "数据明细宽表id", required = true) @RequestParam Long tableId,
            @NotNull(message = "派生指标id不能为空") @Parameter(description = "派生指标id", required = true) @RequestParam Long deriveId,
            @NotEmpty(message = "维度json数据不能为空") @Parameter(description = "维度json数据", required = true) @RequestBody LinkedHashMap<String, Object> dimensionMap) throws Exception {
        metricDataService.deleteDeriveData(tableId, deriveId, dimensionMap);
        return Result.ok();
    }

    @Operation(summary = "修正派生指标数据")
    @PutMapping("/correct-derive-data")
    public <IN, ACC, OUT> Result<Void> updateDeriveData(
            @NotNull(message = "维度数据和表数据不能为空") @Parameter(description = "维度数据和表数据", required = true) @RequestBody @Validated UpdateMetricData<IN, ACC, OUT> updateMetricData) throws Exception {
        metricDataService.correctDeriveData(updateMetricData);
        return Result.ok();
    }

}
