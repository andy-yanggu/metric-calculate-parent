package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.pojo.TableData;
import com.yanggu.metric_calculate.service.MetricDataService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;

@Validated
@RestController
@Api(tags = "指标数据接口")
@RequestMapping("/metric-data")
public class MetricDataController {

    @Autowired
    private MetricDataService metricDataService;

    @ApiOperation("查询单个派生指标数据（实时数据）")
    @PostMapping("/query-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public ApiResponse<Object> queryDeriveData(@NotNull(message = "数据明细宽表id不能为空") @ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                               @NotNull(message = "派生指标id不能为空") @ApiParam("派生指标id") @RequestParam Long deriveId,
                                               @NotEmpty(message = "维度json数据不能为空") @ApiParam(value = "维度json数据") @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        DeriveMetricCalculateResult<Object> result = metricDataService.queryDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success(result);
    }

    @ApiOperation("全量填充（计算所有派生指标数据）")
    @PostMapping("/full-fill-derive-data")
    public ApiResponse<Object> fullUpdate(@NotNull(message = "数据明细宽表id不能为空") @ApiParam("数据明细宽表id") @NotNull @RequestParam Long tableId,
                                          @NotEmpty(message = "数据list不能为空") @ApiParam("数据list") @NotEmpty @RequestBody List<JSONObject> dataList) {
        metricDataService.fullFillDeriveData(dataList, tableId);
        return ApiResponse.success();
    }

    @ApiOperation("部分填充（单个派生指标）")
    @PostMapping("/fill-derive-data-by-id")
    public ApiResponse<Object> fillDeriveDataById(@NotNull(message = "数据明细宽表id不能为空") @ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                                  @NotNull(message = "派生指标id不能为空") @ApiParam("派生指标id") @RequestParam Long deriveId,
                                                  @NotEmpty(message = "数据list不能为空") @ApiParam("数据list") @RequestBody List<JSONObject> dataList) {
        metricDataService.fillDeriveDataById(tableId, deriveId, dataList);
        return ApiResponse.success();
    }

    @ApiOperation("删除派生指标数据")
    @DeleteMapping("/delete-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public ApiResponse<Object> deleteDeriveData(@NotNull(message = "数据明细宽表id不能为空") @ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                                @NotNull(message = "派生指标id不能为空") @ApiParam("派生指标id") @RequestParam Long deriveId,
                                                @NotEmpty(message = "维度json数据不能为空") @ApiParam(value = "维度json数据") @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        metricDataService.deleteDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success();
    }

    @ApiOperation("修正派生指标数据")
    @PutMapping("/correct-derive-data")
    public ApiResponse<Object> updateDeriveData(@NotNull(message = "维度数据和表数据不能为空") @ApiParam("维度数据和表数据") @RequestBody @Validated TableData tableData) {
        metricDataService.correctDeriveData(tableData);
        return ApiResponse.success();
    }

}
