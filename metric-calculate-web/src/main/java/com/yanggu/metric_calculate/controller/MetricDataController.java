package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.table.Table;
import com.yanggu.metric_calculate.pojo.DimensionTableData;
import com.yanggu.metric_calculate.service.MetricDataService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 指标数据接口
 */
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
    public ApiResponse<Object> queryDeriveData(@ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                               @ApiParam("派生指标id") @RequestParam Long deriveId,
                                               @ApiParam(value = "维度json数据") @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        DeriveMetricCalculateResult<Object> result = metricDataService.queryDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success(result);
    }

    @ApiOperation("全量铺底（计算所有派生指标数据）")
    @PostMapping("/full-update-derive")
    public ApiResponse<Object> fullUpdate(@RequestParam Long tableId, @RequestBody List<JSONObject> dataList) {
        metricDataService.fullUpdate(dataList, tableId);
        return ApiResponse.success();
    }

    @ApiOperation("删除派生指标数据")
    @DeleteMapping("/delete-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public ApiResponse<Object> deleteDeriveData(@ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                                @ApiParam("派生指标id") @RequestParam Long deriveId,
                                                @ApiParam(value = "维度json数据") @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        metricDataService.deleteDeriveData(tableId, deriveId, dimensionMap);
        return ApiResponse.success();
    }

    @ApiOperation("更新派生指标数据")
    @PutMapping("/update-derive-data")
    public ApiResponse<Object> updateDeriveData(@ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                                @ApiParam("派生指标id") @RequestParam Long deriveId,
                                                @ApiParam("维度数据和表数据") @RequestBody DimensionTableData dimensionTableData) {
        metricDataService.updateDeriveData(tableId, deriveId, dimensionTableData.getDimensionMap(), dimensionTableData.getTable());
        return ApiResponse.success();
    }

}
