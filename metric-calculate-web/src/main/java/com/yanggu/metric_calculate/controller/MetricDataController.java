package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.yanggu.metric_calculate.client.magiccube.MagicCubeClient;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private MagicCubeClient magiccubeClient;

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @ApiOperation("查询单个派生指标数据（实时数据）")
    @PostMapping("/query-derive-data")
    @DynamicParameters(name = "dimensionJson", properties = {
            @DynamicParameter(name = "name1", value = "value1", example = "key是维度名, value是维度值"),
            @DynamicParameter(name = "name2", value = "value2", example = "多个维度写多个kv")
    })
    public ApiResponse<Object> queryDeriveData(@ApiParam("数据明细宽表id") @RequestParam Long tableId,
                                               @ApiParam("派生指标id") @RequestParam Long deriveId,
                                               @ApiParam(value = "维度json数据") @RequestBody LinkedHashMap<String, Object> dimensionMap) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();

        DataDetailsWideTable table = magiccubeClient.getTableAndMetricByTableId(tableId);
        if (table == null || table.getId() == null) {
            throw new RuntimeException("传入的tableId: " + tableId + "有误");
        }

        List<Derive> deriveList = table.getDerive();
        if (CollUtil.isEmpty(deriveList)) {
            return apiResponse;
        }
        Derive derive = deriveList.stream()
                .filter(tempDerive -> deriveId.equals(tempDerive.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("传入的派生指标id" + deriveId + "有误"));

        DimensionSet dimension = new DimensionSet();
        dimension.setKey(tableId + "_" + derive.getId());
        dimension.setMetricName(derive.getName());
        dimension.setDimensionMap(dimensionMap);
        MetricCube<Object, Object, Object> metricCube = deriveMetricMiddleStore.get(dimension);
        if (metricCube == null) {
            return apiResponse;
        }

        DeriveMetricCalculateResult<Object> query = metricCube.query();
        apiResponse.setData(query);
        return apiResponse;
    }

}
